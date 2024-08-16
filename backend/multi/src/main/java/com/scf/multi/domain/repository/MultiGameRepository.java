package com.scf.multi.domain.repository;

import com.scf.multi.domain.model.MultiGameRoom;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class MultiGameRepository {

    private final Map<String, MultiGameRoom> gameRooms = Collections.synchronizedMap(
        new HashMap<>());

    public List<MultiGameRoom> findAllRooms() {
        return gameRooms.values().stream().toList();
    }

    public MultiGameRoom findOneById(String roomId) {
        return gameRooms.get(roomId);
    }

    public void addRoom(MultiGameRoom room) {
        gameRooms.put(room.getRoomId(), room);
    }

    public void deleteRoom(String roomId) {
        gameRooms.remove(roomId);
    }
}
