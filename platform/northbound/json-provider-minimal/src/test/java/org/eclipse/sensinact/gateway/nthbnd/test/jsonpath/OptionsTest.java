package org.eclipse.sensinact.gateway.nthbnd.test.jsonpath;

import org.junit.Test;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.JsonPath.using;
import static com.jayway.jsonpath.Option.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertThat;

public class OptionsTest extends BaseTestConfiguration {

    @Test(expected = PathNotFoundException.class)
    public void a_leafs_is_not_defaulted_to_null() {

        Configuration conf = Configuration.defaultConfiguration();

        assertThat(using(conf).parse("{\"foo\" : \"bar\"}", false).read("$.baz")).isNull();
    }

    @Test
    public void a_leafs_can_be_defaulted_to_null() {

        Configuration conf = Configuration.builder().options(DEFAULT_PATH_LEAF_TO_NULL).build();

        assertThat(using(conf).parse("{\"foo\" : \"bar\"}", false).read("$.baz", Object.class)).isNull();
    }

    @Test
    public void a_definite_path_is_not_returned_as_list_by_default() {

        Configuration conf = Configuration.defaultConfiguration();

        assertThat(using(conf).parse("{\"foo\" : \"bar\"}", false).read("$.foo")).isInstanceOf(String.class);
    }

    @Test
    public void a_definite_path_can_be_returned_as_list() {

        Configuration conf = Configuration.builder().options(ALWAYS_RETURN_LIST).build();

        assertThat(using(conf).parse("{\"foo\" : \"bar\"}", false).read("$.foo")).isInstanceOf(List.class);

        assertThat(using(conf).parse("{\"foo\": null}", false).read("$.foo")).isInstanceOf(List.class);

        assertThat(using(conf).parse("{\"foo\": [1, 4, 8]}", false).read("$.foo")).asList()
                .containsExactly(Arrays.asList(1, 4, 8));
    }

    @Test
    public void an_indefinite_path_can_be_returned_as_list() {
        Configuration conf = Configuration.builder().options(ALWAYS_RETURN_LIST).build();

        List<Object> result = using(conf).parse("{\"bar\": {\"foo\": null}}", false).read("$..foo");
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isNull();

        assertThat(using(conf).parse("{\"bar\": {\"foo\": [1, 4, 8]}}", false).read("$..foo")).asList()
                .containsExactly(Arrays.asList(1, 4, 8));
    }

    @Test
    public void a_path_evaluation_is_returned_as_VALUE_by_default() {
        Configuration conf = Configuration.defaultConfiguration();

        assertThat(using(conf).parse("{\"foo\" : \"bar\"}", false).read("$.foo")).isEqualTo("bar");
    }

    @Test
    public void a_path_evaluation_can_be_returned_as_PATH_LIST() {
        Configuration conf = Configuration.builder().options(AS_PATH_LIST).build();

        List<String> pathList = using(conf).parse("{\"foo\" : \"bar\"}", false).read("$.foo");

        assertThat(pathList).containsOnly("$['foo']");
    }

    @Test
    public void multi_properties_are_merged_by_default() {

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("a", "a");
        model.put("b", "b");
        model.put("c", "c");

        Configuration conf = Configuration.defaultConfiguration();

        Map<String, Object> result = using(conf).parse(model, false).read("$.['a', 'b']");

        //assertThat(result).isInstanceOf(List.class);
        //assertThat((List)result).containsOnly("a", "b");

        assertThat(result)
                .containsEntry("a", "a")
                .containsEntry("b", "b");
    }

    @Test
    public void when_property_is_required_exception_is_thrown() {
        List<Map<String, String>> model = asList(singletonMap("a", "a-val"),singletonMap("b", "b-val"));

        Configuration conf = Configuration.defaultConfiguration();

        assertThat(using(conf).parse(model, false).read("$[*].a", List.class)).containsExactly("a-val");


        conf = conf.addOptions(Option.REQUIRE_PROPERTIES);

        try{
            using(conf).parse(model, false).read("$[*].a", List.class);
            fail("Should throw PathNotFoundException");
        } catch (PathNotFoundException pnf){}
    }

    @Test
    public void when_property_is_required_exception_is_thrown_2() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("a", singletonMap("a-key", "a-val"));
        model.put("b", singletonMap("b-key", "b-val"));

        Configuration conf = Configuration.defaultConfiguration();

        assertThat(using(conf).parse(model, false).read("$.*.a-key", List.class)).containsExactly("a-val");


        conf = conf.addOptions(Option.REQUIRE_PROPERTIES);

        try{
            using(conf).parse(model, false).read("$.*.a-key", List.class);
            fail("Should throw PathNotFoundException");
        } catch (PathNotFoundException pnf){}
    }


    @Test
    public void issue_suppress_exceptions_does_not_break_indefinite_evaluation() {
        Configuration conf = Configuration.builder().options(SUPPRESS_EXCEPTIONS).build();

        assertThat(using(conf).parse("{\"foo2\": [5]}", false).read("$..foo2[0]")).asList().containsOnly(5);
        assertThat(using(conf).parse("{\"foo\" : {\"foo2\": [5]}}", false).read("$..foo2[0]")).asList().containsOnly(5);
        assertThat(using(conf).parse("[null, [{\"foo\" : {\"foo2\": [5]}}]]", false).read("$..foo2[0]")).asList().containsOnly(5);

        assertThat(using(conf).parse("[null, [{\"foo\" : {\"foo2\": [5]}}]]", false).read("$..foo.foo2[0]")).asList().containsOnly(5);

        assertThat(using(conf).parse("{\"aoo\" : {}, \"foo\" : {\"foo2\": [5]}, \"zoo\" : {}}", false).read("$[*].foo2[0]")).asList().containsOnly(5);
    }

    @Test
    public void isbn_is_defaulted_when_option_is_provided() {
        List<String> result1 = JsonPath.using(JSON_ORG_CONFIGURATION).parse(BaseTestJson.JSON_DOCUMENT, false).read("$.store.book.*.isbn");

        assertThat(result1).containsExactly("0-553-21311-3","0-395-19395-8");

        List<String> result2 = JsonPath.using(JSON_ORG_CONFIGURATION.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)).parse(BaseTestJson.JSON_DOCUMENT, false).read("$.store.book.*.isbn");

        assertThat(result2).containsExactly(null, null, "0-553-21311-3", "0-395-19395-8");
    }
}