package pro.akosarev.showcase;

import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InMemTaskRepository implements TaskRepository {

    private final List<Task> tasks = new LinkedList<>() {{
        this.add(new Task("Первая задача"));
        this.add(new Task("Вторая задача"));
    }};

    @Override
    public List<Task> findAll() {
        return this.tasks;
    }

    @Override
    public void save(Task task) {
        this.tasks.add(task);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return this.tasks.stream()
                .filter(task -> task.id().equals(id))
                .findFirst();
    }
}
