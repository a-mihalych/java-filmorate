package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendDbStorageTests {

    private final FriendDbStorage friendStorage;
    private final UserDbStorage userStorage;

	@Test
	public void addFriendTest() {
		User user1 = User.builder()
				.email("email1@yandex.ru")
				.login("Login1")
				.name("Name1")
				.birthday(LocalDate.of(2000, 2, 22))
				.build();
		user1 = userStorage.createUser(user1);
		int id1 = user1.getId();
		User user2 = User.builder()
				.email("email2@yandex.ru")
				.login("Login2")
				.name("Name2")
				.birthday(LocalDate.of(2000, 2, 22))
				.build();
		user2 = userStorage.createUser(user2);
		int id2 = user2.getId();
		assertEquals(0, userStorage.getFriendsForIdUser(id1).size(),
				     "Количество пользователей в списке друзей не совпадает");
		friendStorage.addFriend(id1, id2);
		assertEquals(1, userStorage.getFriendsForIdUser(id1).size(),
				     "Количество пользователей в списке друзей не совпадает");
		userStorage.deleteUser(id1);
		userStorage.deleteUser(id2);
	}

	@Test
	public void deleteFriendTest() {
		User user1 = User.builder()
				.email("email1@yandex.ru")
				.login("Login1")
				.name("Name1")
				.birthday(LocalDate.of(2000, 2, 22))
				.build();
		user1 = userStorage.createUser(user1);
		int id1 = user1.getId();
		User user2 = User.builder()
				.email("email2@yandex.ru")
				.login("Login2")
				.name("Name2")
				.birthday(LocalDate.of(2000, 2, 22))
				.build();
		user2 = userStorage.createUser(user2);
		int id2 = user2.getId();
		friendStorage.addFriend(id1, id2);
		assertEquals(1, userStorage.getFriendsForIdUser(id1).size(),
				     "Количество пользователей в списке друзей не совпадает");
		friendStorage.deleteFriend(id1, id2);
		assertEquals(0, userStorage.getFriendsForIdUser(id1).size(),
				     "Количество пользователей в списке друзей не совпадает");
		userStorage.deleteUser(id1);
		userStorage.deleteUser(id2);
	}
}
