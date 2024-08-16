package com.scf.battle.application;

import com.scf.battle.domain.dto.Room.CreateRoomDTO;
import com.scf.battle.domain.dto.Room.RoomResponseDTO;
import com.scf.battle.domain.dto.User.Player;
import com.scf.battle.domain.model.BattleGameRoom;
import com.scf.battle.domain.repository.BattleGameRepository;
import com.scf.battle.global.error.ErrorCode;
import com.scf.battle.global.error.exception.BusinessException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final BattleGameRepository battleGameRepository;
    private final  UserService userService;
    public List<BattleGameRoom> findAllRooms() {
        return battleGameRepository.findAllRooms();
    }
    private final Map<String, BattleGameRoom> roomMap = new ConcurrentHashMap<>();

    public BattleGameRoom findById(String roomId) {
        return battleGameRepository.findById(roomId);
    }

    public void joinRoom(String roomId, Long userId, String username, String roomPassword) {
        BattleGameRoom room = battleGameRepository.findById(roomId);
        if (room == null) {
            throw new BusinessException(roomId, "roomId", ErrorCode.ROOM_NOT_FOUND);
        }
        if (room.getPlayerB() != null && room.getPlayerB().getUserId() != null) {
            throw new BusinessException(roomId, "roomId", ErrorCode.MAX_PLAYERS_EXCEEDED);
        }
        if (!"ssafy".equals(room.getPassword()) && !room.getPassword().equals(roomPassword)) {
            throw new BusinessException(roomPassword, "roomPassword", ErrorCode.PASSWORD_MISMATCH);
        }
        battleGameRepository.joinRoom(roomId, userId, username, roomPassword);
    }

    public String createRoom(Long memberId, String username, CreateRoomDTO createRoomDTO) {
        String roomId = UUID.randomUUID().toString();
        Player playerHost = new Player(memberId, username, 100);
        String password = (createRoomDTO.getPassword() != null) ? createRoomDTO.getPassword() : "ssafy";
        BattleGameRoom room = BattleGameRoom.builder()
                .roomId(roomId)
                .hostId(memberId)
                .hostUsername(username)
                .isStart(false)
                .finalRound(createRoomDTO.getRound())
                .title(createRoomDTO.getTitle())
                .password(password)
                .playerA(playerHost)
                .hasPlayerASubmitted(false)
                .hasPlayerBSubmitted(false)
                .isAttack(false)
                .currentRound(0)
                .hostCharacter(userService.getUserCharaterType(memberId))
                .build();
        roomMap.put(roomId, room);
        battleGameRepository.addRoom(room);
        return roomId;
    }

    public void removeRoom(String roomId) {
        battleGameRepository.removeRoom(roomId);
    }

    public List<RoomResponseDTO> convertToRoomResponseDTOs(List<BattleGameRoom> rooms) {
        return rooms.stream()
                .map(this::convertToRoomResponseDTO)
                .collect(Collectors.toList());
    }

    private RoomResponseDTO convertToRoomResponseDTO(BattleGameRoom room) {
        return RoomResponseDTO.builder()
                .roomId(room.getRoomId())
                .hostId(room.getHostId())
                .hostUsername(room.getHostUsername())
                .title(room.getTitle())
                .maxPlayer(2)
                .curPlayer(determineCurrentPlayers(room))
                .isLock(!"ssafy".equals(room.getPassword()))
                .build();
    }


    private int determineCurrentPlayers(BattleGameRoom room) {
        return (room.getPlayerB() != null && room.getPlayerB().getUserId() != null) ? 2 : 1;
    }

    public void handleUserDisconnect(String roomId, Long userId) {
        BattleGameRoom room = roomMap.get(roomId);
        if (room != null) {
            // 플레이어를 제거하는 로직
            if (room.getPlayerA().getUserId().equals(userId)) {
                room.setPlayerA(null);
            } else if (room.getPlayerB().getUserId().equals(userId)) {
                room.setPlayerB(null);
            }

            // 방에 남아 있는 플레이어가 없으면 방 삭제
            if (room.getPlayerA() == null && room.getPlayerB() == null) {
                roomMap.remove(roomId);
                // 방 삭제 로그 또는 추가 작업
                System.out.println("Room " + roomId + " has been deleted because all players disconnected.");
            }
        }
    }
}
