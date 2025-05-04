package team.incube.gwangjutalentfestivalserver.domain.user.entity;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String encodedPassword;
}
