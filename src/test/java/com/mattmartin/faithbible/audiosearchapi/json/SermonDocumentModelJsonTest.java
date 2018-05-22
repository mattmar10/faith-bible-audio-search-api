package com.mattmartin.faithbible.audiosearchapi.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mattmartin.faithbible.audiosearchapi.config.FaithDateTimeFormatter;
import com.mattmartin.faithbible.audiosearchapi.config.JacksonConfiguration;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonDocumentModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.SermonMediaModel;
import com.mattmartin.faithbible.audiosearchapi.elasticsearch.models.StatsModel;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SermonDocumentModelJsonTest {

    @Test
    public void testParsing() throws IOException {
        final String json = "{\n" +
                "        \"id\": \"fakeId\",\n" +
                "        \"title\": \"Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015\",\n" +
                "        \"href\": \"http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3\",\n" +
                "        \"speaker\": \"Dr Mark Hitchcock\",\n" +
                "        \"serviceDay\": \"Sunday Morning\",\n" +
                "        \"date\": \"2015-06-21T00:00:00\",\n" +
                "        \"series\": \"Father's Day\",\n" +
                "        \"stats\": {\"plays\": 3, \"likes\": 5, \"shares\": 7},\n" +
                "        \"tags\": [\"Fathers Day\", \"Exodus\"],\n" +
                "        \"media\": {\n" +
                "            \"pdf\": \"http://edmondfaithbible.com/?page_id=2743&download&file_name=2015_0621%20Fathers%20Day%20MH-FBC%20SunAM.pdf\",\n" +
                "            \"mp3\": \"http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3\"\n" +
                "        }\n" +
                "    }";

        final ObjectMapper mapper = new JacksonConfiguration().objectMapper();
        final SermonDocumentModel parsed = mapper.readValue(json, SermonDocumentModel.class);

        final SermonMediaModel mediaModel =
                new SermonMediaModel("http://edmondfaithbible.com/?page_id=2743&download&file_name=2015_0621%20Fathers%20Day%20MH-FBC%20SunAM.pdf",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3");

        final StatsModel statsModel = new StatsModel(3, 5, 7);
        final Set<String> tags = new HashSet<>(Arrays.asList("Fathers Day", "Exodus"));
        final SermonDocumentModel manual =
                new SermonDocumentModel(
                        "fakeId",
                        "Exodus 20:12 How to Make Your Father's Day on Father's Day MH-FBC SunAM 6/21/2015",
                        "http://edmondfaithbible.com/?page_id=2743&show&file_name=2015_0621%20Fathers%20Day%20Exodus%2020_12.mp3",
                        "Dr Mark Hitchcock",
                        FaithDateTimeFormatter.getLocalDate("2015-06-21"),
                        "Father's Day",
                        mediaModel,
                        Optional.of("fathersDay123"),
                        Optional.of(statsModel),
                        Optional.empty(),
                        Optional.of(tags));

        assertThat(parsed, equalTo(manual));
        assertThat(parsed.hashCode(), equalTo(manual.hashCode()));
        assertThat(parsed.hashCode(), equalTo(manual.hashCode()));
        assertThat(parsed.getTags().get(), equalTo(tags));
        StatsModel stats = parsed.getStats().get();

        assertThat(stats.getPlays().get(), is(3));
        assertThat(stats.getLikes().get(), is(5));
        assertThat(stats.getShares().get(), is(7));

    }
}
