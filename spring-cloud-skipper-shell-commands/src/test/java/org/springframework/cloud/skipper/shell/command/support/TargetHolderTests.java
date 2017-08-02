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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Pollack
 */
public class TargetHolderTests {

	@Test
	public void defaultConstructor() {
		TargetHolder targetHolder = new TargetHolder();
		assertThat(targetHolder.getTarget()).isNotNull();
		assertThat(targetHolder.getTarget().getTargetUri())
				.hasHost(Target.DEFAULT_HOST)
				.hasScheme(Target.DEFAULT_SCHEME)
				.hasPort(Target.DEFAULT_PORT);

		Target target = new Target("http://localhost:9494");
		targetHolder = new TargetHolder(target);
		assertThat(targetHolder.getTarget().getTargetUri())
				.hasScheme("http")
				.hasHost("localhost")
				.hasPort(9494);
		target = new Target("https://localhost:8080");
		targetHolder = new TargetHolder(target);
		assertThat(targetHolder.getTarget().getTargetUri())
				.hasScheme("https")
				.hasHost("localhost")
				.hasPort(8080);
	}
}
