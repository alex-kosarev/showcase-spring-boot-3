package pro.akosarev.showcase;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.UUID;

@Component
public class ApplicationUserDetailsService extends MappingSqlQuery<ApplicationUser>
        implements UserDetailsService {

    public ApplicationUserDetailsService(DataSource ds) {
        super(ds, "select * from t_application_user where c_username = :username");
        this.declareParameter(new SqlParameter("username", Types.VARCHAR));
        this.compile();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.executeByNamedParam(Map.of("username", username)).stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find user " + username));
    }

    @Override
    protected ApplicationUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ApplicationUser(rs.getObject("id", UUID.class),
                rs.getString("c_username"), rs.getString("c_password"));
    }
}
