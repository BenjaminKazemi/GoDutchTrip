import com.google.common.collect.ImmutableMap;
import helper.GenericFunctionalTest;
import models.Participant;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Router;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ParticipantsControllerTest extends GenericFunctionalTest {

    @Before
    public void before() {
        Participant.deleteAll();
    }

    @Test
    public void create() throws IllegalAccessException, UnsupportedEncodingException {
        Participant participant = new Participant( "Username", "Password", "EMail", "Full Name" );
        Http.Response response = post("/participants", participant);

        assertEquals( 200, (Object) response.status);

        Participant returnedParticipant = Participant.fromJson( getContent( response ) );

        assertNotNull(returnedParticipant.id);
        assertEquals(participant.username, returnedParticipant.username);
        assertEquals(participant.email, returnedParticipant.email);
        assertEquals(participant.fullName, returnedParticipant.fullName);
    }

    @Test
    public void read() throws UnsupportedEncodingException, IllegalAccessException {
        Participant participant = new Participant( "Username", "Password", "EMail", "Full Name" );
        Http.Response response = post("/participants", participant);

        Participant returnedParticipant = Participant.fromJson( getContent(response) );

        returnedParticipant = Participant.fromJson( getContent( get(Router.reverse("ParticipantsController.read", ImmutableMap.of("id", (Object) returnedParticipant.id)).url) ) );

        assertNotNull(returnedParticipant);
        assertEquals(participant.username, returnedParticipant.username);
        assertEquals(participant.email, returnedParticipant.email);
        assertEquals(participant.fullName, returnedParticipant.fullName);
    }

    @Test
    public void update() throws UnsupportedEncodingException, IllegalAccessException {
        Participant participant = new Participant( "Username", "Password", "EMail", "Full Name" );
        Participant returnedBowl = Participant.fromJson(getContent(post("/participants", participant)));
        participant.username += " UPDATED";
        participant.email += " UPDATED";
        participant.fullName += " UPDATED";

        returnedBowl = Participant.fromJson( getContent( put(Router.reverse( "ParticipantsController.update", ImmutableMap.of("id", (Object) returnedBowl.id ) ).url, participant) ) );
        assertNotNull( returnedBowl );
        assertNotNull( returnedBowl.id );
        assertEquals( participant.username, returnedBowl.username );
        assertEquals( participant.email, returnedBowl.email );
        assertEquals( participant.fullName, returnedBowl.fullName );

        returnedBowl = Participant.fromJson( getContent( get(Router.reverse("ParticipantsController.read", ImmutableMap.of("id", (Object) returnedBowl.id)).url) ) );
        assertNotNull( returnedBowl );
        assertEquals( participant.username, returnedBowl.username );
        assertEquals( participant.email, returnedBowl.email );
        assertEquals( participant.fullName, returnedBowl.fullName );
    }

    @Test
    public void delete() throws IllegalAccessException, UnsupportedEncodingException {
        Participant participant = new Participant( "Username", "Password", "EMail", "Full Name" );
        participant = Participant.fromJson( getContent( post( "/participants", participant ) ) );

        Http.Response response = delete(Router.reverse("ParticipantsController.delete", ImmutableMap.of("id", (Object) participant.id)).url);
        assertEquals(200, (Object)response.status );

        response = GET(Router.reverse("ParticipantsController.read", ImmutableMap.of("id", (Object) participant.id)).url);
        assertEquals( Http.StatusCode.NOT_FOUND, (Object)response.status );
    }

    @Test
    public void list() throws IllegalAccessException, UnsupportedEncodingException {
        List<Participant> bowls = Participant.listFromJson( getContent( get( Router.reverse( "ParticipantsController.list" ).url ) ) );
        assertEquals( 0, bowls.size() );

        for( int i = 0 ; i< 10; i++ ) {
            Participant.fromJson( getContent( post( "/participants", new Participant( "Username" + i, "Password" + i, "EMail" + i, "Full Name" + i ) ) ) );
        }

        bowls = Participant.listFromJson(getContent(get(Router.reverse("ParticipantsController.list").url)));
        assertEquals( 10, bowls.size() );
    }

}