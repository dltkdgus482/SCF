package com.scf.battle.domain.repository;

import com.scf.battle.domain.model.BattleGameRoom;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class BattleGameRepository {

    private final Map<String, BattleGameRoom> gameRooms = Collections.synchronizedMap(
        new HashMap<>());

    public List<BattleGameRoom> findAllRooms() {
        System.out.println(gameRooms);
        return gameRooms.values().stream().toList();

    }

    public BattleGameRoom findById(String roomId) {
        return gameRooms.get(roomId);
    }

    public void joinRoom(String roomId, Long userId, String username, String roomPassword) {
        BattleGameRoom room = gameRooms.get(roomId);
        room.add(userId, username, roomPassword);
    }

    public void addRoom(BattleGameRoom gameRoom) {
        gameRooms.put(gameRoom.getRoomId(), gameRoom);
    }

    public void removeRoom(String roomid){
        gameRooms.remove(roomid);
    }
}
