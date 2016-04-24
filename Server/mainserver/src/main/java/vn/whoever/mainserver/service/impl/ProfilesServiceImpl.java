package vn.whoever.mainserver.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import v.whoever.service.impl.GenerateIdImpl;
import vn.whoever.mainserver.dao.ProfilesDao;
import vn.whoever.mainserver.model.Profiles;
import vn.whoever.mainserver.service.ProfilesService;

@Service("profilesService")
@Transactional
public class ProfilesServiceImpl implements ProfilesService {

	@Autowired
	private ProfilesDao profilesDao;

	public String generateIdProfile() {
		return GenerateIdImpl.generateId().getId();
	}

	public boolean updateProfile(Profiles profile) {
		try {
			profilesDao.updateProfiles(profile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setProfile(Profiles profile) {
		try {
			profilesDao.setProfiles(profile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Profiles getProfile(String idUser) {
		return profilesDao.getProfiles(idUser);
	}

	public String getIdUser(String idProfile) {
		return profilesDao.getIdUser(idProfile);
	}

	public String getIdProfile(String idUser) {
		return profilesDao.getIdProfile(idUser);
	}

	public String getNickName(String idUser) {
		return profilesDao.getNickname(idUser);
	}

	public List<Profiles> queryIdUserByNickName(String queryNickName) {
		return profilesDao.queryIdUserByNickname(queryNickName);
	}	

}
