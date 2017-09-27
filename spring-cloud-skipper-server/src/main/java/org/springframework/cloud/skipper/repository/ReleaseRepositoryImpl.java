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
package org.springframework.cloud.skipper.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.skipper.domain.Release;
import org.springframework.cloud.skipper.domain.StatusCode;
import org.springframework.cloud.skipper.index.PackageException;

/**
 * @author Mark Pollack
 * @author Ilayaperumal Gopinathan
 */
public class ReleaseRepositoryImpl implements ReleaseRepositoryCustom {

	@Autowired
	private ReleaseRepository releaseRepository;

	@Override
	public Release findLatestRelease(String releaseName) {
		Release latestRelease = this.releaseRepository.findTopByNameOrderByVersionDesc(releaseName);
		if (latestRelease == null) {
			throw new PackageException(String.format("Can not find a latest release named '%s'", releaseName));
		}
		return latestRelease;
	}

	@Override
	public Release findByNameAndVersion(String releaseName, int version) {
		Iterable<Release> releases = this.releaseRepository.findAll();

		Release matchingRelease = null;
		for (Release release : releases) {
			if (release.getName().equals(releaseName) && release.getVersion() == version) {
				matchingRelease = release;
				break;
			}
		}
		if (matchingRelease == null) {
			throw new PackageException(String.format("Can not find release '%s', version '%s'", releaseName, version));
		}
		return matchingRelease;
	}

	@Override
	public List<Release> findReleaseRevisions(String releaseName, int revisions) {
		int latestVersion = findLatestRelease(releaseName).getVersion();
		int lowerVersion = latestVersion - Integer.valueOf(revisions);
		return this.releaseRepository.findByNameAndVersionBetweenOrderByNameAscVersionDesc(releaseName,
				lowerVersion + 1, latestVersion);
	}

	@Override
	public List<Release> findLatestDeployedOrFailed(String releaseName) {
		return getDeployedOrFailed(this.releaseRepository.findByNameIgnoreCaseContaining(releaseName));
	}

	@Override
	public List<Release> findLatestDeployedOrFailed() {
		return getDeployedOrFailed(this.releaseRepository.findAll());
	}

	private List<Release> getDeployedOrFailed(Iterable<Release> allReleases) {
		List<Release> releases = new ArrayList<>();
		for (Release release : allReleases) {
			StatusCode releaseStatusCode = release.getInfo().getStatus().getStatusCode();
			if (releaseStatusCode.equals(StatusCode.DEPLOYED) || releaseStatusCode.equals(StatusCode.FAILED)) {
				releases.add(release);
			}
		}
		return releases;
	}
}
