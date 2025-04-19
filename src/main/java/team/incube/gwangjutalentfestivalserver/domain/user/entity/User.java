package team.incube.gwangjutalentfestivalserver.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import team.incube.gwangjutalentfestivalserver.domain.user.enums.Role;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {

	@Id
	@UuidGenerator
	@GeneratedValue
	private UUID id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String encodedPassword;

	@ElementCollection
	private List<Role> roles;
}
