package vn.whoever.mainserver.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import v.whoever.service.impl.GenerateIdImpl;
import vn.whoever.mainserver.dao.ContactUserDao;
import vn.whoever.mainserver.dao.ContactsDao;
import vn.whoever.mainserver.dao.StatusDao;
import vn.whoever.mainserver.dao.UsersDao;
import vn.whoever.mainserver.model.Status;
import vn.whoever.mainserver.model.Users;
import vn.whoever.mainserver.service.StatusService;
import vn.whoever.support.model.request.GetStatus;
import vn.whoever.support.model.utils.Order;

@Service("statusService")
@Transactional
public class StatusServiceImpl implements StatusService {

	@Autowired
	private StatusDao statusDao;
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private ContactsDao contactsDao;
	
	@Autowired
	private ContactUserDao contactUserDao;
	
	public String generateStatusId() {
		return GenerateIdImpl.generateId().getId();
	}
	
	public boolean postStatus(Status status) {
		try {
			statusDao.postStatus(status);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * This is heart of Whoever project.
	 * @param getStatus
	 * @return
	 */
	public List<Status> getListStatus(GetStatus getStatus) {
		String idUser = usersDao.findIdUser(getStatus.getSsoId());
		List<String> lIdFriend = contactUserDao.getListIdFriend(contactsDao.getIdContact(idUser));
		List<Status> listStatus = null;
		if(getStatus.getOrder() == Order.friends) {
			listStatus = statusDao.getListStatusByFriends(lIdFriend, getStatus.getOffset());
		} else {
			listStatus = statusDao.getListStatusContainNearby(lIdFriend, 
					getStatus.getLocation().getxLoc(), getStatus.getLocation().getyLoc(), getStatus.getOffset());
		}
		System.out.println("Do lon chuoi: " + listStatus.size());
		return listStatus;
	}

	public Status getDetailStatus(String idStatus) {
		// TODO Auto-generated method stub
		return null;
	}

}
