package pro.akosarev.showcase;

import java.util.UUID;

public record Task(UUID id, String details, boolean completed, UUID applicationUserId) {

    public Task(String details, UUID id) {
        this(UUID.randomUUID(), details, false, id);
    }
}
