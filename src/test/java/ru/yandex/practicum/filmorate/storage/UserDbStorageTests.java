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
class UserDbStorageTests {

	private final UserDbStorage userStorage;

	@Test
	public void getUserTest() {
		User user = User.builder()
				.email("email@yandex.ru")
				.login("Login")
				.name("Name")
				.birthday(LocalDate.of(2000, 2, 22))
				.build();
		user = userStorage.createUser(user);
		int id = user.getId();
		assertEquals("Login", userStorage.getUser(id).getLogin(), "Логин не совпадает");
		userStorage.deleteUser(id);
	}

	@Test
	public void getUsersTest() {
		User user = User.builder()
				.email("email@yandex.ru")
				.login("Login")
				.name("Name")
				.birthday(LocalDate.of(2000, 2, 22))
				.build();
		user = userStorage.createUser(user);
		int id = user.getId();
		assertEquals(1, userStorage.getUsers().size(),
				     "Количество пользователей в списке не совпадает");
		userStorage.deleteUser(id);
	}

	@Test
	public void createUserTest() {
		assertEquals(0, userStorage.getUsers().size(),
				     "Количество пользователей в списке не совпадает");
		User user = User.builder()
				.email("email@yandex.ru")
				.login("Login")
				.name("Name")
				.birthday(LocalDate.of(2000, 2, 22))
				.build();
		user = userStorage.createUser(user);
		int id = user.getId();
		assertEquals(1, userStorage.getUsers().size(),
				     "Количество пользователей в списке не совпадает");
		userStorage.deleteUser(id);
	}

	@Test
	public void saveUserTest() {
		User user1 = User.builder()
				.email("email@yandex.ru")
				.login("Login")
				.name("Name")
				.birthday(LocalDate.of(2000, 2, 22))
				.build();
		user1 = userStorage.createUser(user1);
		int id1 = user1.getId();
		User user2 = User.builder()
				.id(id1)
				.email("emailNew@yandex.ru")
				.login("LoginNew")
				.name("Name")
				.birthday(LocalDate.of(2000, 2, 22))
				.build();
		userStorage.saveUser(user2);
		int id2 = user2.getId();
		assertEquals(id2, userStorage.getUser(id1).getId(), "Индефикатор не совпадает");
		assertEquals("LoginNew", userStorage.getUser(id1).getLogin(), "Логин не совпадает");
		userStorage.deleteUser(id2);
	}

	@Test
	public void deleteUserTest() {
		User user = User.builder()
				.email("email@yandex.ru")
				.login("Login")
				.name("Name")
				.birthday(LocalDate.of(2000, 2, 22))
				.build();
		user = userStorage.createUser(user);
		int id = user.getId();
		assertEquals(1, userStorage.getUsers().size(),
				     "Количество пользователей в списке не совпадает");
		userStorage.deleteUser(id);
		assertEquals(0, userStorage.getUsers().size(),
				     "Количество пользователей в списке не совпадает");
	}

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
		assertEquals(0, userStorage.getIdFriendsForIdUser(id1).size(),
				     "Количество пользователей в списке друзей не совпадает");
		userStorage.addFriend(id1, id2);
		assertEquals(1, userStorage.getIdFriendsForIdUser(id1).size(),
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
		userStorage.addFriend(id1, id2);
		assertEquals(1, userStorage.getIdFriendsForIdUser(id1).size(),
				     "Количество пользователей в списке друзей не совпадает");
		userStorage.deleteFriend(id1, id2);
		assertEquals(0, userStorage.getIdFriendsForIdUser(id1).size(),
				     "Количество пользователей в списке друзей не совпадает");
		userStorage.deleteUser(id1);
		userStorage.deleteUser(id2);
	}

	@Test
	public void getIdFriendsForIdUserTest() {
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
		assertEquals(0, userStorage.getIdFriendsForIdUser(id1).size(),
				     "Количество пользователей в списке друзей не совпадает");
		userStorage.addFriend(id1, id2);
		assertEquals(1, userStorage.getIdFriendsForIdUser(id1).size(),
				     "Количество пользователей в списке друзей не совпадает");
		userStorage.deleteUser(id1);
		userStorage.deleteUser(id2);
	}
}
