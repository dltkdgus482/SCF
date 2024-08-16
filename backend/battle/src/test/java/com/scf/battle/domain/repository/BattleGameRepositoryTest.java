package com.scf.battle.domain.repository;

import com.scf.battle.domain.model.BattleGameRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BattleGameRepositoryTest {

    private BattleGameRepository battleGameRepository;

    @BeforeEach
    public void setUp() {
        battleGameRepository = new BattleGameRepository();
    }

    @Test
    public void testFindAllRooms() {
        BattleGameRoom room1 = BattleGameRoom.builder().roomId(UUID.randomUUID().toString()).build();

        battleGameRepository.addRoom(room1);

        List<BattleGameRoom> rooms = battleGameRepository.findAllRooms();

        assertEquals(1, rooms.size());
    }

    @Test
    public void testFindById() {
        String roomId = UUID.randomUUID().toString();
        BattleGameRoom room = BattleGameRoom.builder().roomId(roomId).build();

        battleGameRepository.addRoom(room);

        BattleGameRoom foundRoom = battleGameRepository.findById(roomId);

        assertNotNull(foundRoom);
        assertEquals(roomId, foundRoom.getRoomId());
    }

//    @Test
//    public void testJoinRoom() {
//        String roomId = UUID.randomUUID().toString();
//        BattleGameRoom room = BattleGameRoom.builder().roomId(roomId).password("1234").build();
//
//        battleGameRepository.addRoom(room);
//
//        JoinRoomDTO joinRoomDTO = JoinRoomDTO.builder()
//            .userId(1L)
//            .username("user1")
//            .roomPassword("1234")
//            .build();
//
//        battleGameRepository.joinRoom(roomId, joinRoomDTO);
//
//        BattleGameRoom updatedRoom = battleGameRepository.findById(roomId);
//        assertEquals(1, updatedRoom.getPlayers().size());
//    }

    @Test
    public void testAddRoom() {
        String roomId = UUID.randomUUID().toString();
        BattleGameRoom room = BattleGameRoom.builder().roomId(roomId).build();

        battleGameRepository.addRoom(room);

        BattleGameRoom foundRoom = battleGameRepository.findById(roomId);

        assertNotNull(foundRoom);
        assertEquals(roomId, foundRoom.getRoomId());
    }
}
