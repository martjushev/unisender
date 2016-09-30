package com.unisender;

import com.unisender.entities.EmailMessage;
import com.unisender.entities.MailList;
import com.unisender.exceptions.UniSenderConnectException;
import com.unisender.requests.BatchSendEmailRequest;
import com.unisender.responses.ResponseWithWarnings;
import com.unisender.responses.SendEmailResponse;
import com.unisender.responses.SendEmailResponseError;
import com.unisender.responses.Warning;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author AYeremenok
 */
public class UnisenderTest {

    private static final String MAIL_1 = "to1@test.com";
    private static final String MAIL_2 = "to2@test.com";
    private static final String MAIL_3 = "to3@test.com";
    private static final String SENDER_NAME = "Sender";
    private static final String SENDER_EMAIL = "from@test.com";
    private static final String SUBJECT_1 = "Subject1";
    private static final String SUBJECT_2 = "Subject2";
    private static final String SUBJECT_3 = "Subject3";
    private static final String BODY_1 = "<html>body1</html>";
    private static final String BODY_2 = "<html>body2</html>";
    private static final String BODY_3 = "<html>body3</html>";
    private static final String USER_CAMPAIGN_ID = UUID.randomUUID().toString();
    private static final String REPLY_TO = "noreply@test.tt";

    private UniSender uniSender;
    private MailList mailList;

    private String nextResponse;
    private String lastPostQuery;

    @Before
    public void setUp() throws Exception {
        uniSender = new UniSender("apikey") {
            @Override
            protected String execute(URL url, String postQuery) throws UniSenderConnectException {
                lastPostQuery = postQuery;
                return nextResponse;
            }
        };
        mailList = new MailList(0);
    }

    @Test
    public void testBatchSend() throws Exception {
        Map<String, EmailMessage> messages = createMap(
                MAIL_1, new EmailMessage(SENDER_NAME, SENDER_EMAIL, SUBJECT_1, BODY_1),
                MAIL_2, new EmailMessage(SENDER_NAME, SENDER_EMAIL, SUBJECT_2, BODY_2),
                MAIL_3, new EmailMessage(SENDER_NAME, SENDER_EMAIL, SUBJECT_3, BODY_3)
        );
        messages.get(MAIL_1).setReplyTo(REPLY_TO);

        BatchSendEmailRequest request = new BatchSendEmailRequest(messages, mailList, null, null, null, USER_CAMPAIGN_ID);

        nextResponse = "{\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"id\": \"8394628274\",\n" +
                "            \"index\": 0,\n" +
                "            \"email\": \"to1@test.com\",\n" +
                "            \"acceptDate\": \"2016-09-29 08:18:12\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"8394628275\",\n" +
                "            \"index\": 1,\n" +
                "            \"errors\": [\n" +
                "                {\n" +
                "                    \"message\": \"Указанный язык не поддерживается системой.\",\n" +
                "                    \"accept_date\": \"2016-09-27 08:18:12\",\n" +
                "                    \"code\": \"unsupported_lang\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"message\": \"Email данному адресату уже был отправлен\",\n" +
                "                    \"accept_date\": \"2016-09-28 08:18:12\",\n" +
                "                    \"code\": \"has_been_sent\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"email\": \"to2@test.com\",\n" +
                "            \"acceptDate\": \"2016-09-28 08:18:12\"\n" +
                "        }\n" +
                "    ]," +
                "    \"warnings\": [\n" +
                "      {\n" +
                "        \"index\": \"2\",\n" +
                "        \"email\": \"to3@test.com\",\n" +
                "        \"warning\": \"SZ150219-06 to3@test.com Адрес отписан от любых писем в списке 1\"\n" +
                "      }\n" +
                "    ]\n" +
                "}";
        ResponseWithWarnings<List<SendEmailResponse>> responseWithWarnings = uniSender.batchSendEmailReturnWarnings(request);
        List<SendEmailResponse> responses = responseWithWarnings.getResponse();

        assertEquals(2, responses.size());
        SendEmailResponse response1 = responses.get(0);
        SendEmailResponse response2 = responses.get(1);

        assertEquals(0, response1.getErrors().size());
        assertEquals(MAIL_1, response1.getEmail());

        List<SendEmailResponseError> response2Errors = response2.getErrors();
        assertEquals(2, response2Errors.size());
        assertEquals("unsupported_lang", response2Errors.get(0).getCode());
        assertEquals("Указанный язык не поддерживается системой.", response2Errors.get(0).getMessage());
        assertEquals("has_been_sent", response2Errors.get(1).getCode());
        assertEquals("Email данному адресату уже был отправлен", response2Errors.get(1).getMessage());
        assertEquals(MAIL_2, response2.getEmail());

        List<Warning> warnings = responseWithWarnings.getWarnings();
        assertEquals(1, warnings.size());
        assertEquals(2, warnings.get(0).getIndex());
        assertEquals(MAIL_3, warnings.get(0).getEmail());
        assertEquals("SZ150219-06 to3@test.com Адрес отписан от любых писем в списке 1", warnings.get(0).getWarning());

        assertLastQueryContains("email[0]=" + MAIL_1);
        assertLastQueryContains("subject[0]=" + SUBJECT_1);
        assertLastQueryContains("body[0]=" + BODY_1);
        assertLastQueryContains("email[1]=" + MAIL_2);
        assertLastQueryContains("subject[1]=" + SUBJECT_2);
        assertLastQueryContains("body[1]=" + BODY_2);
        assertLastQueryContains("user_campaign_id=" + USER_CAMPAIGN_ID);
        assertLastQueryContains("headers[0]=Reply-To:" + REPLY_TO);
    }

    @Test
    public void testCheckEmail() throws Exception {
        Set<String> ids = new HashSet<String>(asList("59847824", "59847829", "59847834"));
        nextResponse =
                "{\"result\":{\"statuses\":[" +
                        "{\"id\":\"59847824\",\"status\":\"ok_sent\"}," +
                        "{\"id\":\"59847829\",\"status\":\"err_not_sent\"}," +
                        "{\"id\":\"59847834\",\"status\":\"ok_sent\"}" +
                "]}}";
        Map<String, String> response = uniSender.checkEmail(ids);
        assertLastQueryContains("59847824");
        assertLastQueryContains("59847829");
        assertLastQueryContains("59847834");

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("59847824", "ok_sent");
        expected.put("59847829", "err_not_sent");
        expected.put("59847834", "ok_sent");
        assertEquals(expected, response);
    }

    private void assertLastQueryContains(String substring) throws UnsupportedEncodingException {
        String decodedQuery = URLDecoder.decode(lastPostQuery, UniSender.API_ENCODING);
        assertTrue(String.format("%s\n does not contain %s", decodedQuery, substring), decodedQuery.contains(substring));
    }

    private static <K, V> Map<K, V> createMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new LinkedHashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }
}
