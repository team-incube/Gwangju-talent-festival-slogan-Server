package team.incube.gwangjutalentfestivalserver.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import team.incube.gwangjutalentfestivalserver.global.security.filter.JwtFilter;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtProvider jwtProvider;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		JwtFilter jwtFilter = new JwtFilter(jwtProvider);

		return http
			.authorizeHttpRequests(it -> it
				.requestMatchers("/slogan/**").permitAll()
				.requestMatchers("/auth/**").permitAll()
				.requestMatchers("/admin/**").authenticated()
				.anyRequest().denyAll()
			)
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement (it ->
				it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			).cors(it -> it.configurationSource(corsConfig()))
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	CorsConfigurationSource corsConfig() {
		CorsConfiguration corsConfigurationSource = new CorsConfiguration();
		corsConfigurationSource.addAllowedHeader("*");
		corsConfigurationSource.addAllowedMethod("*");
		corsConfigurationSource.addAllowedOriginPattern("*");
		corsConfigurationSource.setAllowCredentials(true);
		corsConfigurationSource.addExposedHeader("Authorization");

		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfigurationSource);
		return urlBasedCorsConfigurationSource;
	}
}
