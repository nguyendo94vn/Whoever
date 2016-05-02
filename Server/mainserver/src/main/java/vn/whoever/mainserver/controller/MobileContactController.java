package vn.whoever.mainserver.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.whoever.mainserver.model.Profiles;
import vn.whoever.mainserver.model.Users;
import vn.whoever.mainserver.service.AuthToken;
import vn.whoever.mainserver.service.ContactsService;
import vn.whoever.mainserver.service.ProfilesService;
import vn.whoever.mainserver.service.UsersService;
import vn.whoever.support.response.ReturnContact;
import vn.whoever.support.response.ReturnSearchContact;
import vn.whoever.support.utils.TimeUp;

@Controller
@RequestMapping("/mobile/friends")
public class MobileContactController {

	@Autowired
	private ContactsService contactService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private ProfilesService profilesService;
	
	@Autowired
	private AuthToken authToken;
	
	@RequestMapping(value = {"/"}, method = RequestMethod.GET,
			produces = "application/json")
	public @ResponseBody List<ReturnContact> getFriends(HttpServletRequest request) {
		List<ReturnContact> returnContacts = new LinkedList<ReturnContact>();
		List<String> idFriends = contactService.getListIdFriends(request);
		for (String idFriend : idFriends) {
			ReturnContact contact = new ReturnContact();
			Users users = usersService.findByIdUser(idFriend);
			contact.setSsoId(users.getSsoId());
			contact.setLatestOnline((new TimeUp(users.getTimeUp()).getTime()));
			contact.setNickName(profilesService.getNickName(idFriend));
			returnContacts.add(contact);
		}
		return returnContacts;
	}
	
	@RequestMapping(value = {"/{ssoId}"}, method = RequestMethod.POST,
			consumes = "application/json", produces = "application/json")
	public @ResponseBody void addFriend(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(value = "ssoId") String ssoId) {
		System.out.println("AddFriends: " + ssoId);
		try {
			contactService.addFriend(authToken.getIdUserHttp(request), ssoId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = {"/{ssoId}"}, method = RequestMethod.DELETE)
	public String deleteFriend(@PathVariable(value = "ssoId") String ssoId) {
		
		return "";
	}
	
	@RequestMapping(value = {"/search/{query}"}, method = RequestMethod.GET,
			consumes = "application/json", produces = "application/json")
	public @ResponseBody List<ReturnSearchContact> queryContact(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "query") String query) {
		Map<String, ReturnSearchContact> mapReturn = new HashMap<String, ReturnSearchContact>();
		/**
		 * search theo nick name v� ssoId
		 */
		for(Users user : usersService.queryIdUserBySsoId(query)) {
			if(!user.getIsAnonymous()) {
				ReturnSearchContact contact = new ReturnSearchContact(user.getSsoId(), null, null, false);
				contact.setAvatart(null);
				contact.setNickName(profilesService.getNickName(user.getIdUser()));
				mapReturn.put(user.getIdUser(), contact);
			}
		}
		for(Profiles profile : profilesService.queryIdUserByNickName(query)) {
			if(mapReturn.get(profile.getIdUser()) == null) {
				ReturnSearchContact contact = new ReturnSearchContact(null, profile.getNickName(), null, false);
				contact.setSsoId(usersService.findSsoIdbyIdUser(profile.getIdUser()));
				mapReturn.put(profile.getIdUser(), contact);
			}
		}
		
		for(String idFriend : contactService.getListIdFriends(request)) {
			if(mapReturn.get(idFriend) != null) {
				mapReturn.get(idFriend).setIsFriend(true);
			}
		}
		
		List<ReturnSearchContact> returnList = new ArrayList<ReturnSearchContact>();
		for (Entry<String, ReturnSearchContact> item : mapReturn.entrySet()) {
			returnList.add(item.getValue());
		}
		
		System.out.println("size return: " + returnList.size());
		return returnList;
	}
}
