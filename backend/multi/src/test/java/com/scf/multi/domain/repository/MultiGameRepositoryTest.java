package com.scf.multi.domain.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.scf.multi.domain.model.MultiGameRoom;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MultiGameRepositoryTest {

    private MultiGameRepository repository;
    private MultiGameRoom gameRoom;

    @BeforeEach
    void setUp() {
        repository = new MultiGameRepository();
        gameRoom = MultiGameRoom.builder()
            .roomId("abc-def")
            .hostId(1L)
            .title("First Room")
            .maxPlayer(2)
            .password("1234")
            .build();
    }

    @Test
    @DisplayName("방이 정상적으로 추가되어야 한다.")
    void AddRoomTest() {

        // When
        repository.addRoom(gameRoom);

        MultiGameRoom room = repository.findOneById("abc-def");

        // Then
        assertNotNull(room);
        assertEquals("abc-def", room.getRoomId());
        assertEquals(1L, room.getHostId());
        assertEquals("First Room", room.getTitle());
    }

    @Test
    @DisplayName("방의 목록을 정상적으로 반환해야 한다.")
    void FindAllRoomsTest() {

        // Given
        MultiGameRoom anotherRoom = MultiGameRoom.builder()
            .roomId("room2")
            .hostId(2L)
            .title("Second Room")
            .maxPlayer(4)
            .password("5678")
            .build();

        repository.addRoom(gameRoom);
        repository.addRoom(anotherRoom);

        // When
        List<MultiGameRoom> rooms = repository.findAllRooms();

        // Then
        assertEquals(2, rooms.size());
        assertTrue(rooms.contains(gameRoom));
        assertTrue(rooms.contains(anotherRoom));
    }


    @Test
    @DisplayName("roomId에 해당하는 방을 정상적으로 반환해야 한다.")
    void FindOneByIdTest() {

        // Given
        repository.addRoom(gameRoom);

        // When
        MultiGameRoom room = repository.findOneById("abc-def");

        // Then
        assertNotNull(room);
        assertEquals("abc-def", room.getRoomId());
        assertEquals(1L, room.getHostId());
    }


    @Test
    @DisplayName("roomId에 해당하는 방을 정상적으로 삭제해야 한다.")
    void DeleteRoomTest() {
        // Given
        repository.addRoom(gameRoom);

        // When
        repository.deleteRoom("abc-def");
        MultiGameRoom room = repository.findOneById("abc-def");

        // Then
        assertNull(room);
    }
}
