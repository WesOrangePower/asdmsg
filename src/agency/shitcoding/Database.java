package agency.shitcoding;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
    HashMap<String, Long> keys = new HashMap<>();
    HashMap<Long, UserData> users = new HashMap<>();

    HashMap<Long, Chat> chats = new HashMap<>();

    final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    // There may be multiple readLocks. Blocks writing.
    private final Lock readLock = lock.readLock();
    // There may not be any other locks. Blocks reading and writing.
    private final Lock writeLock = lock.writeLock();

    public Database() {

    }

    public void readLock(boolean lock) {
        if (lock)
            readLock.lock();
        else
            readLock.unlock();
    }

    public void writeLock(boolean lock) {
        if (lock)
            writeLock.lock();
        else
            writeLock.unlock();
    }

    public UserData signIn(String key, Integer threadID) {
        Long UID = keys.get(key);
        UserData data = null;

        if (UID != null) {
            data = users.get(UID);
            data.threadID = threadID;
            Main.LOGGER.debug(String.format("User %s (%s) has logged in.", UID, data.name));
        }
        return data;
    }

    public Long signUp(String key) {
        Long UID = createRandomID(users.keySet());

        keys.put(key, UID);
        UserData userData = new UserData();
        userData.id = UID;
        users.put(UID, userData);

        Main.LOGGER.info("User " + UID + " was created for " + key);

        return UID;
    }

    public Long createRandomID(Collection<Long> usedIDs) {
        Random rng = new Random();
        Long potentialUID = rng.nextLong();
        while (true) {
            if (!usedIDs.contains(potentialUID))
                return potentialUID;
        }
    }

    static class UserData implements Cloneable {
        Long id;
        String name;
        List<Integer> chats;

        // '-1' means offline.
        Integer threadID = -1;

        @Override
        public UserData clone() {
            try {
                return (UserData) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    static class Chat {
        ChatType type;

        public enum ChatType {
            GroupChat, DirectChat
        }
    }
}