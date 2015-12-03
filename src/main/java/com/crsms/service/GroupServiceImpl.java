package com.crsms.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crsms.dao.GroupDao;
import com.crsms.domain.Group;
import com.crsms.domain.User;
import com.crsms.dto.UserIdAndEmailDto;

@Service("groupService")
@Transactional
public class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService {
	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserService	userService;
	
	@Override
	public void subscribe(Long groupId, User user) {
		Group group = groupDao.getById(groupId);
		group.addUser(user);
		groupDao.update(group);
	}
	
	@Override
	public void subscribe(Long groupId, String email) {
		User user = userService.getUserByEmail(email);
		this.subscribe(groupId, user);
	}

	@Override
	public void unsubscribe(Long groupId, String email) {
		Group group = groupDao.getById(groupId);
		User user = userService.getUserByEmail(email);
		group.deleteUser(user);
		groupDao.update(group);
	}

	@Override
	public List<Group> getAllByCourseId(Long courseId) {
		return groupDao.getAllByCourseId(courseId);
	}

	@Override
	public void save(Long courseId, Group group) {
		group.setCourse(courseService.getById(courseId));
		groupDao.save(group);
	}

	@Override
	public void deleteById(Long groupId) {
		groupDao.deleteById(groupId);
	}

	@Override
	public void update(Long courseId, Group group) {
		group.setCourse(courseService.getById(courseId));
		groupDao.update(group);
	}

	@Override
	public List<UserIdAndEmailDto> getStudentsIdsAndEmailsFromGroup(Long groupId) {
		return groupDao.getStudentsIdsAndEmailsFromGroup(groupId);
	}

	@Override
	public List<String> addStudents(Long courseId, Long groupId, Set<String> emails) {
		if (emails.isEmpty()) {
			throw new IllegalArgumentException("No emails");
		}
		
		List<String> subscribedUsersEmails = selectAlreadySubscribedUsers(courseId, emails);
		emails.removeAll(subscribedUsersEmails);
		if (emails.isEmpty()) {
			return subscribedUsersEmails;
		}
		
		User user = null;
		for (String email : emails) {
			if (!userService.isEmailExists(email)) {
				userService.createAndSaveStudent(email, email);
			}
			user = userService.getUserByEmail(email);
			this.subscribe(groupId, user);
		}
		
		return subscribedUsersEmails;
	}
	
	@Override
	public List<String> selectAlreadySubscribedUsers(Long courseId, Set<String> emails) {
		return groupDao.selectAlreadySubscribedUsers(courseId, emails);
	}

}