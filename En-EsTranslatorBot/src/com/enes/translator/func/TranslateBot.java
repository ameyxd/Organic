package com.enes.translator.func;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.annotations.OnMsg;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class TranslateBot {
	public static final String botemail = "teamchatbot1@gmail.com";
	public static final String botpassword = "ameyambade";
	public static String email1, lang1, name1;
	public static String email2, lang2, name2;

	public static void main(String[] args) {
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail(botemail).setPassword(botpassword)
				.startReceivingEvents(new TranslateBot());

	}

	@OnKeyword("start")
	
	public void Register(TeamchatAPI api) {
		
		email1 = api.context().currentSender().getEmail();
		name1 = api.context().currentSender().getName();
		String msg = "Select your preferred language & enter the email of the user you want to talk to.";
		Form f = api.objects().form();
		f.addField(api.objects().select().label("Language")
				.addOption("English").addOption("Spanish").addOption("French")
				.addOption("German").name("language1"));
		f.addField(api.objects().input().label("Email").name("mail"));
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion(msg).setReplyScreen(f)
						.setReplyLabel("Enter").showDetails(true)
						.alias("register")));
	}

	String l1 = new String();
	String l2 = new String();

	@OnAlias("register")
	public void Create(TeamchatAPI api) {
		lang1 = api.context().currentReply().getField("language1");
		if (lang1.equals("English")) {
			l1 = "en";
		} else if (lang1.equals("French")) {
			l1 = "fr";
		} else if (lang1.equals("Spanish")) {
			l1 = "es";
		} else if (lang1.equals("German")) {
			l1 = "de";
		}

		email2 = api.context().currentReply().getField("mail");
		String msg = name1
				+ " wants to talk to you! Select your preferred language to accept the request.";
		Form f = api.objects().form();
		f.addField(api.objects().select().label("Language")
				.addOption("English").addOption("Spanish").addOption("French")
				.addOption("German").name("language2"));
		api.perform(api
				.context()
				.create()
				.setName(name1)
				.add(email2)
				.post(new PrimaryChatlet().setQuestion(msg).setReplyScreen(f)
						.setReplyLabel("Enter").showDetails(true)
						.alias("create")));
	}

	@OnAlias("create")
	public void onCreate(TeamchatAPI api) {
		lang2 = api.context().currentReply().getField("language2");
		name2 = api.context().currentReply().senderName();
		if (lang2.equals("English")) {
			l2 = "en";
		} else if (lang2.equals("French")) {
			l2 = "fr";
		} else if (lang2.equals("Spanish")) {
			l2 = "es";
		} else if (lang2.equals("German")) {
			l2 = "de";
		}

		api.perform(api
				.context()
				.create()
				.setName(name2)
				.add(email1)
				.post(new TextChatlet(
						"Registration Successful. Start conversation.")));
	}

	@OnMsg
	public void msgReceived(TeamchatAPI api) throws ClientProtocolException,
			IOException {
		String email = api.context().currentSender().getEmail();
		String msg = api.context().currentChatlet().raw();
		
		//To detect language directly
		/*
		 * String newlang = new String ();
		newlang = translator.detectLang(msg);
		System.out.println(newlang);
		*/

		if (email.equals(email1)) {
			String retmsg1 = translator.translate(msg, l1, l2);

			api.perform(api.context().create().setName(name1).add(email2)
					.post(new TextChatlet(retmsg1)));
		}
		if (email.equals(email2)) {
			String retmsg2 = translator.translate(msg, l2, l1);

			api.perform(api.context().create().setName(name2).add(email1)
					.post(new TextChatlet(retmsg2)));
		}
		
	}
}
