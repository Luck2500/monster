package monsterservice.controller;

import monsterservice.handleExceptionError.HandleExceptionError;
import monsterservice.model.Monster;
import monsterservice.service.MonsterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
public class MonsterControllerTest {

    private Monster mockMonster() {
        Monster mockMonster = new Monster();
        mockMonster.setId(1);
        mockMonster.setName("LeeFast");
        mockMonster.setHealth(750);
        return mockMonster;
    }

    @InjectMocks
    private MonsterController monsterController;

    @Mock
    private MonsterService monsterService;

    @Test
    void getGreetingTest() {
        String response = monsterController.getGreeting();
        assertEquals("Hi there!", response);
    }

    @Test
    void postCreateTest() {
        doReturn(mockMonster()).when(monsterService).postCreateMonsterService(any(Monster.class));
        Monster response = monsterController.postCreate(new Monster());
        assertEquals(mockMonster().getId(), response.getId());
        assertEquals(mockMonster().getName(), response.getName());
        assertEquals(mockMonster().getHealth(), response.getHealth());
    }

    @Test
    void getAllMonsterTest() {
        List<Monster> monsterList = new ArrayList<>();
        monsterList.add(mockMonster());

        doReturn(monsterList).when(monsterService).getAllMonsterService();

        List<Monster> response = monsterController.getAll();
        assertEquals(monsterList, response);
    }

    @Test
    void getInformationTest() {
        Optional<Monster> monsterOptional = Optional.of(mockMonster());

        doReturn(monsterOptional).when(monsterService).getInformation(any(Integer.class));

        Optional<Monster> response = monsterController.getInformation(1);

        assertTrue(response.isPresent());
    }

    @Test
    void putUpdateTestSuccess() throws HandleExceptionError {
        doReturn(mockMonster())
                .when(monsterService)
                .updateMonsterByIdService(any(Monster.class));
        ResponseEntity<Monster> response
                = monsterController.putUpdate(new Monster());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMonster().getId(),
                Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    void putUpdateTestFail() throws HandleExceptionError {
        doThrow(new HandleExceptionError("Error"))
                .when(monsterService)
                .updateMonsterByIdService(any(Monster.class));

        ResponseEntity<Monster> response
                = monsterController.putUpdate(new Monster());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    void deleteTestSuccess() throws HandleExceptionError {
        //Given
        doReturn(true)
                .when(monsterService)
                .deleteMonsterService(any(Integer.class));
        //when
        ResponseEntity<Boolean> response = monsterController.delete(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
        //assertEquals(true,response.getBody());
        //assertTrue(response.getBody());
    }

    @Test
    void deleteTestFail() throws HandleExceptionError {
        doThrow(new HandleExceptionError("Error"))
                .when(monsterService)
                .deleteMonsterService(any(Integer.class));

        ResponseEntity<Boolean> response = monsterController.delete(1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Boolean.FALSE, response.getBody());
    }

    @Test
    void putAttackTestSccess() throws HandleExceptionError {
        doReturn("Update success")
                .when(monsterService)
                .attackMonsterService(any(Integer.class),
                        any(Integer.class));

        ResponseEntity<String> response
                = monsterController.putAttack(1, 100);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update success", response.getBody());
    }

    @Test
    void putAttackTestFailCanNotUpdate() throws HandleExceptionError {
        doThrow(new HandleExceptionError("Update Success"))
                .when(monsterService)
                .attackMonsterService(any(Integer.class), any(Integer.class));
        ResponseEntity<String> response = monsterController.putAttack(1, 100);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Update Success", response.getBody());
    }

    @Test
    void putAttackTestFailDataNotFound() throws HandleExceptionError {
        doThrow(new HandleExceptionError("Data not found"))
                .when(monsterService)
                .attackMonsterService(any(Integer.class),
                        any(Integer.class));
        ResponseEntity<String> response = monsterController.putAttack(1, 100);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Data not found", response.getBody());
    }
}
