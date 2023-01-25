package pro.akosarev.showcase;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcOperationsTaskRepository implements TaskRepository, RowMapper<Task> {

    private final JdbcOperations jdbcOperations;

    public JdbcOperationsTaskRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Task> findAll() {
        return this.jdbcOperations.query("select * from t_task", this);
    }

    @Override
    public void save(Task task) {
        this.jdbcOperations.update("""
                insert into t_task(id, c_details, c_completed, id_application_user) values (?, ?, ?, ?)
                """, new Object[]{task.id(), task.details(), task.completed(), task.applicationUserId()});
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return this.jdbcOperations.query("select * from t_task where id = ?", new Object[]{id}, this)
                .stream().findFirst();
    }

    @Override
    public List<Task> findByApplicationUserId(UUID id) {
        return this.jdbcOperations.query("select * from t_task where id_application_user = ?", this, id);
    }

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Task(rs.getObject("id", UUID.class),
                rs.getString("c_details"),
                rs.getBoolean("c_completed"),
                rs.getObject("id_application_user", UUID.class));
    }
}
