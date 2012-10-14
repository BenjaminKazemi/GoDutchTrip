import com.google.common.collect.ImmutableMap;
import helper.GenericFunctionalTest;
import models.Bowl;
import models.Expense;
import models.Participant;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Router;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BowlsControllerTest extends GenericFunctionalTest {

    @Before
    public void before() {
        Expense.deleteAll();
        deleteAllBowlParticipants();
        Bowl.deleteAll();
    }

    public static void deleteAllBowlParticipants() {
        List<Bowl> bowls = Bowl.findAll();
        for( Bowl bowl:bowls ) {
            bowl.participants.clear();
            bowl.save();
        }
    }

    @Test
    public void create() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        Http.Response response = post(Router.reverse(BowlsController_create).url, "bowl", bowl);

        assertEquals( 200, (Object) response.status);

        Bowl returnedBowl = Bowl.fromJson( getContent( response ) );

        assertNotNull( returnedBowl.id );
        assertEquals(bowl.title, returnedBowl.title);
        assertEquals(bowl.description, returnedBowl.description);
        assertEquals(bowl.date.toString(), returnedBowl.date.toString());
    }

    @Test
    public void createWithParticipants() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );

        List<Long> participants = new ArrayList<Long>(2);
        participants.add( Participant.fromJson( getContent( post(Router.reverse(ParticipantsController_create).url, "participant", new Participant( "Username1", "Password1", "EMail1", "Full Name1" )) ) ).id );
        participants.add( Participant.fromJson( getContent( post(Router.reverse(ParticipantsController_create).url, "participant", new Participant( "Username2", "Password2", "EMail2", "Full Name2" )) ) ).id );

        Http.Response response = post(Router.reverse(BowlsController_create).url, "bowl", bowl, "participants", participants);

        assertEquals( 200, (Object) response.status);

        Bowl returnedBowl = Bowl.fromJson( getContent( response ) );

        assertNotNull( returnedBowl.id );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals( bowl.date.toString(), returnedBowl.date.toString() );
        assertEquals( participants.size(), returnedBowl.participants.size() );
    }

    @Test
    public void read() throws UnsupportedEncodingException, IllegalAccessException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        Http.Response response = post(Router.reverse(BowlsController_create).url, "bowl", bowl);

        Bowl returnedBowl = Bowl.fromJson(getContent(response));

        returnedBowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) returnedBowl.id)).url) ) );

        assertNotNull( returnedBowl );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals(bowl.date.toString(), returnedBowl.date.toString());
    }

    @Test
    public void update() throws UnsupportedEncodingException, IllegalAccessException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        Bowl returnedBowl = Bowl.fromJson(getContent(post(Router.reverse(BowlsController_create).url, "bowl", bowl)));
        bowl.title += " UPDATED";
        bowl.description += " UPDATED";
        bowl.date = new Date();

        returnedBowl = Bowl.fromJson( getContent( put(Router.reverse( BowlsController_update, ImmutableMap.of("id", (Object) returnedBowl.id ) ).url, bowl) ) );
        assertNotNull( returnedBowl );
        assertNotNull( returnedBowl.id );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals( bowl.date.toString(), returnedBowl.date.toString() );

        returnedBowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) returnedBowl.id)).url) ) );
        assertNotNull( returnedBowl );
        assertEquals( bowl.title, returnedBowl.title );
        assertEquals( bowl.description, returnedBowl.description );
        assertEquals( bowl.date.toString(), returnedBowl.date.toString() );
    }

    @Test
    public void delete() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post( Router.reverse(BowlsController_create).url, "bowl", bowl ) ) );

        Http.Response response = delete(Router.reverse(BowlsController_delete, ImmutableMap.of("id", (Object) bowl.id)).url);
        assertEquals(200, (Object)response.status );

        response = GET(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url);
        assertEquals( Http.StatusCode.NOT_FOUND, (Object)response.status );
    }

    @Test
    public void list() throws IllegalAccessException, UnsupportedEncodingException {
        List<Bowl> bowls = Bowl.listFromJson( getContent( get( Router.reverse( BowlsController_list ).url ) ) );
        assertEquals( 0, bowls.size() );

        for( int i = 0 ; i< 10; i++ ) {
            Bowl.fromJson( getContent( post( Router.reverse(BowlsController_create).url, "bowl", new Bowl( "chicago Trip " + i, "description " + i, new Date() ) ) ) );
        }

        bowls = Bowl.listFromJson(getContent(get(Router.reverse(BowlsController_list).url)));
        assertEquals( 10, bowls.size() );
    }

    @Test
    public void addParticipant() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );

        Participant participant = new Participant( "Username", "Password", "EMail", "Full Name" );
        participant = Participant.fromJson( getContent( post(Router.reverse(ParticipantsController_create).url, "participant", participant) ) );

        bowl = Bowl.fromJson( getContent( put(Router.reverse(BowlsController_addParticipant, ImmutableMap.of("id", (Object) bowl.id, "pId", participant.id)).url) ) );
        assertEquals( 1, bowl.participants.size() );
        assertEquals( bowl.participants.get(0).email, participant.email );
        assertEquals( bowl.participants.get(0).fullName, participant.fullName );
        assertEquals( bowl.participants.get(0).username, participant.username );

        participant = new Participant( "Username", "Password", "EMail", "Full Name" );
        participant = Participant.fromJson( getContent( post(Router.reverse(ParticipantsController_create).url, "participant", participant) ) );

        bowl = Bowl.fromJson( getContent( put(Router.reverse(BowlsController_addParticipant, ImmutableMap.of("id", (Object) bowl.id, "pId", participant.id)).url) ) );
        assertEquals( 2, bowl.participants.size() );

        assertEquals( bowl.participants.get(0).email, participant.email );
        assertEquals( bowl.participants.get(0).fullName, participant.fullName );
        assertEquals( bowl.participants.get(0).username, participant.username );

        assertEquals( bowl.participants.get(1).email, participant.email );
        assertEquals( bowl.participants.get(1).fullName, participant.fullName );
        assertEquals( bowl.participants.get(1).username, participant.username );

    }

    @Test
    public void deleteParticipant() throws IllegalAccessException, UnsupportedEncodingException {
        Bowl bowl = new Bowl( "chicago Trip", "description", new Date() );
        Participant participant = new Participant( "Username", "Password", "EMail", "Full Name" );

        bowl = Bowl.fromJson( getContent( post(Router.reverse(BowlsController_create).url, "bowl", bowl) ) );

        participant = Participant.fromJson( getContent( post( Router.reverse(ParticipantsController_create).url, "participant", participant ) ) );
        bowl = Bowl.fromJson( getContent( put(Router.reverse(BowlsController_addParticipant, ImmutableMap.of("id", (Object) bowl.id, "pId", participant.id)).url) ) );
        assertEquals( 1, bowl.participants.size() );

        participant = Participant.fromJson( getContent( post( Router.reverse(ParticipantsController_create).url, "participant", participant ) ) );
        bowl = Bowl.fromJson( getContent( put(Router.reverse(BowlsController_addParticipant, ImmutableMap.of("id", (Object) bowl.id, "pId", participant.id)).url) ) );
        assertEquals( 2, bowl.participants.size() );

        Http.Response response = delete(Router.reverse(BowlsController_deleteParticipant, ImmutableMap.of("id", (Object) bowl.id, "pId", bowl.participants.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        bowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 1, bowl.participants.size() );

        response = delete(Router.reverse(BowlsController_deleteParticipant, ImmutableMap.of("id", (Object) bowl.id, "pId", bowl.participants.get(0).id)).url);
        assertEquals(200, (Object)response.status );

        bowl = Bowl.fromJson( getContent( get(Router.reverse(BowlsController_read, ImmutableMap.of("id", (Object) bowl.id)).url) ) );
        assertEquals( 0, bowl.participants.size() );
    }

}