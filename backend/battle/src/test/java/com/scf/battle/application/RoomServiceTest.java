package com.scf.battle.application;

import com.scf.battle.domain.dto.Room.CreateRoomDTO;
import com.scf.battle.domain.dto.Room.RoomResponseDTO;
import com.scf.battle.domain.dto.User.Player;
import com.scf.battle.domain.model.BattleGameRoom;
import com.scf.battle.domain.repository.BattleGameRepository;
import com.scf.battle.global.error.ErrorCode;
import com.scf.battle.global.error.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private BattleGameRepository battleGameRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllRooms_success() {
        // Given
        List<BattleGameRoom> rooms = List.of(mock(BattleGameRoom.class));
        when(battleGameRepository.findAllRooms()).thenReturn(rooms);

        // When
        List<BattleGameRoom> result = roomService.findAllRooms();

        // Then
        assertEquals(rooms, result);
    }

    @Test
    void findById_success() {
        // Given
        String roomId = "room1";
        BattleGameRoom room = mock(BattleGameRoom.class);
        when(battleGameRepository.findById(roomId)).thenReturn(room);

        // When
        BattleGameRoom result = roomService.findById(roomId);

        // Then
        assertEquals(room, result);
    }

    @Test
    void joinRoom_roomNotFound() {
        // Given
        String roomId = "room1";
        Long userId = 1L;
        String username = "user1";
        String roomPassword = "password";
        when(battleGameRepository.findById(roomId)).thenReturn(null);

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            roomService.joinRoom(roomId, userId, username, roomPassword);
        });

        // Then
        assertEquals(ErrorCode.ROOM_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void joinRoom_maxPlayersExceeded() {
        // Given
        String roomId = "room1";
        Long userId = 1L;
        String username = "user1";
        String roomPassword = "password";
        BattleGameRoom room = mock(BattleGameRoom.class);
        Player playerB = mock(Player.class);
        when(playerB.getUserId()).thenReturn(2L);
        when(room.getPlayerB()).thenReturn(playerB);
        when(battleGameRepository.findById(roomId)).thenReturn(room);

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            roomService.joinRoom(roomId, userId, username, roomPassword);
        });

        // Then
        assertEquals(ErrorCode.MAX_PLAYERS_EXCEEDED.getMessage(), exception.getMessage());
    }

    @Test
    void joinRoom_passwordMismatch() {
        // Given
        String roomId = "room1";
        Long userId = 1L;
        String username = "user1";
        String roomPassword = "wrongPassword";
        BattleGameRoom room = mock(BattleGameRoom.class);
        when(room.getPassword()).thenReturn("password");
        when(battleGameRepository.findById(roomId)).thenReturn(room);

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            roomService.joinRoom(roomId, userId, username, roomPassword);
        });

        // Then
        assertEquals(ErrorCode.PASSWORD_MISMATCH.getMessage(), exception.getMessage());
    }

    @Test
    void joinRoom_success() {
        // Given
        String roomId = "room1";
        Long userId = 1L;
        String username = "user1";
        String roomPassword = "password";
        BattleGameRoom room = mock(BattleGameRoom.class);
        when(room.getPassword()).thenReturn("password");
        when(battleGameRepository.findById(roomId)).thenReturn(room);

        // When
        roomService.joinRoom(roomId, userId, username, roomPassword);

        // Then
        verify(battleGameRepository).joinRoom(roomId, userId, username, roomPassword);
    }

    @Test
    void createRoom_success() {
        // Given
        Long memberId = 1L;
        String username = "host";
        CreateRoomDTO createRoomDTO = mock(CreateRoomDTO.class);
        when(createRoomDTO.getRound()).thenReturn(3);
        when(createRoomDTO.getTitle()).thenReturn("Test Room");
        when(createRoomDTO.getPassword()).thenReturn("password");

        // When
        String roomId = roomService.createRoom(memberId, username, createRoomDTO);

        // Then
        assertNotNull(roomId);
        verify(battleGameRepository).addRoom(any(BattleGameRoom.class));
    }

    @Test
    void removeRoom_success() {
        // Given
        String roomId = "room1";

        // When
        roomService.removeRoom(roomId);

        // Then
        verify(battleGameRepository).removeRoom(roomId);
    }

    @Test
    void convertToRoomResponseDTOs_success() {
        // Given
        BattleGameRoom room = mock(BattleGameRoom.class);
        when(room.getRoomId()).thenReturn("room1");
        when(room.getHostId()).thenReturn(1L);
        when(room.getTitle()).thenReturn("Test Room");
        when(room.getPassword()).thenReturn("password");
        when(room.getPlayerB()).thenReturn(null);

        List<BattleGameRoom> rooms = List.of(room);

        // When
        List<RoomResponseDTO> result = roomService.convertToRoomResponseDTOs(rooms);

        // Then
        assertEquals(1, result.size());
        assertEquals("room1", result.get(0).getRoomId());
        assertEquals(1L, result.get(0).getHostId());
        assertEquals("Test Room", result.get(0).getTitle());
        assertTrue(result.get(0).getIsLock());
        assertEquals(1, result.get(0).getCurPlayer());
    }
}
