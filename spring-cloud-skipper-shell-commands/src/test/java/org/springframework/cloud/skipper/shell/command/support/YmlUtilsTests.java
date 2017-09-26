/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.skipper.shell.command.support;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Pollack
 */
public class YmlUtilsTests {

	@Test
	public void testSimpleConversion() {
		String stringToConvert = "hello=oi,world=mundo";
		String yml = YmlUtils.convertFromCsvToYaml(stringToConvert);
		assertThat(yml).isEqualTo("hello: oi\nworld: mundo\n");
	}

	@Test
	public void testBasicContent() throws IOException {
		Resource resource =
				new ClassPathResource("/org/springframework/cloud/skipper/shell/command/support/log4j.properties");
		String stringToConvert = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
		String yml = YmlUtils.convertFromCsvToYaml(stringToConvert);
		Resource convertedYmlResource =
				new ClassPathResource("/org/springframework/cloud/skipper/shell/command/support/log4j.yml");
		assertThat(yml).isEqualTo(StreamUtils.copyToString(convertedYmlResource.getInputStream(),
				Charset.defaultCharset()));
	}

}
