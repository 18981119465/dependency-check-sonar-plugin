/*
 * Dependency-Check Plugin for SonarQube
 * Copyright (C) 2015-2017 Steve Springett
 * steve.springett@owasp.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.dependencycheck.base;

import org.codehaus.staxmate.SMInputFactory;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.config.Configuration;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;

public final class DependencyCheckUtils {

    private DependencyCheckUtils() {
    }

    public static SMInputFactory newStaxParser() throws FactoryConfigurationError {
        XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
        xmlFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
        xmlFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
        xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlFactory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        return new SMInputFactory(xmlFactory);
    }

    public static Severity cvssToSonarQubeSeverity(Float cvssScore, Float blocker, Float critical, Float major, Float minor) {
        if (blocker.floatValue() >= 0 && cvssScore.floatValue() >= blocker.doubleValue()) {
            return Severity.BLOCKER;
        } else if (critical.floatValue() >= 0 && cvssScore.floatValue() >= critical.floatValue()) {
            return Severity.CRITICAL;
        } else if (major.floatValue() >= 0 && cvssScore.floatValue() >= major.floatValue()) {
            return Severity.MAJOR;
        } else if (minor.floatValue() >= 0 && cvssScore.floatValue() >= minor.floatValue()) {
            return Severity.MINOR;
        } else {
            return Severity.INFO;
        }
    }

    public static Severity cvssToSonarQubeSeverity(Float cvssScore, Configuration config) {
        Float severityBlocker = config.getFloat(DependencyCheckConstants.SEVERITY_BLOCKER).orElse(DependencyCheckConstants.SEVERITY_BLOCKER_DEFAULT);
        Float severityCritical = config.getFloat(DependencyCheckConstants.SEVERITY_CRITICAL).orElse(DependencyCheckConstants.SEVERITY_CRITICAL_DEFAULT);
        Float severityMajor = config.getFloat(DependencyCheckConstants.SEVERITY_MAJOR).orElse(DependencyCheckConstants.SEVERITY_MAJOR_DEFAULT);
        Float severityMinor = config.getFloat(DependencyCheckConstants.SEVERITY_MINOR).orElse(DependencyCheckConstants.SEVERITY_MINOR_DEFAULT);
        return DependencyCheckUtils.cvssToSonarQubeSeverity(cvssScore, severityBlocker ,severityCritical, severityMajor, severityMinor);
    }

}
