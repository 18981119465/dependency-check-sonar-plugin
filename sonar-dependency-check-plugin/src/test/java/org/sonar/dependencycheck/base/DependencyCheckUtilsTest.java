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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sonar.api.batch.rule.Severity;

import java.util.Arrays;
import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(Parameterized.class)
public class DependencyCheckUtilsTest {

    private final Float cvssSeverity;
    private final Float critical;
    private final Float major;
    private final Severity expectedSeverity;

    public DependencyCheckUtilsTest(Float cvssSeverity, Float critical, Float major, Severity expectedSeverity) {
        this.cvssSeverity = cvssSeverity;
        this.critical = critical;
        this.major = major;
        this.expectedSeverity = expectedSeverity;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> severities() {
        return Arrays.asList(new Object[][]{
                // defaults
                {Float.valueOf("10.0"), Float.valueOf("7.0"), Float.valueOf("4.0"), Severity.CRITICAL},
                {Float.valueOf("7.0"),  Float.valueOf("7.0"), Float.valueOf("4.0"), Severity.CRITICAL},
                {Float.valueOf("6.9"),  Float.valueOf("7.0"), Float.valueOf("4.0"), Severity.MAJOR},
                {Float.valueOf("4.0"),  Float.valueOf("7.0"), Float.valueOf("4.0"), Severity.MAJOR},
                {Float.valueOf("3.9"),  Float.valueOf("7.0"), Float.valueOf("4.0"), Severity.MINOR},
                {Float.valueOf("0.0"),  Float.valueOf("7.0"), Float.valueOf("4.0"), Severity.MINOR},
                
                // custom
                {Float.valueOf("10.0"), Float.valueOf("5.0"), Float.valueOf("2.0"), Severity.CRITICAL},
                {Float.valueOf("7.0"),  Float.valueOf("5.0"), Float.valueOf("2.0"), Severity.CRITICAL},
                {Float.valueOf("6.9"),  Float.valueOf("5.0"), Float.valueOf("2.0"), Severity.CRITICAL},
                {Float.valueOf("4.0"),  Float.valueOf("5.0"), Float.valueOf("2.0"), Severity.MAJOR},
                {Float.valueOf("3.9"),  Float.valueOf("5.0"), Float.valueOf("2.0"), Severity.MAJOR},
                {Float.valueOf("1.9"),  Float.valueOf("5.0"), Float.valueOf("2.0"), Severity.MINOR},
                {Float.valueOf("0.0"),  Float.valueOf("5.0"), Float.valueOf("2.0"), Severity.MINOR},
                
                // custom, critical deactivated
                {Float.valueOf("10.0"), Float.valueOf("-1"),  Float.valueOf("2.0"), Severity.MAJOR},
                {Float.valueOf("7.0"),  Float.valueOf("-1"),  Float.valueOf("2.0"), Severity.MAJOR},
                {Float.valueOf("6.9"),  Float.valueOf("-1"),  Float.valueOf("2.0"), Severity.MAJOR},
                {Float.valueOf("4.0"),  Float.valueOf("-1"),  Float.valueOf("2.0"), Severity.MAJOR},
                {Float.valueOf("3.9"),  Float.valueOf("-1"),  Float.valueOf("2.0"), Severity.MAJOR},
                {Float.valueOf("1.9"),  Float.valueOf("-1"),  Float.valueOf("2.0"), Severity.MINOR},
                {Float.valueOf("0.0"),  Float.valueOf("-1"),  Float.valueOf("2.0"), Severity.MINOR},
                
                // custom, critical and major deactivated
                {Float.valueOf("10.0"), Float.valueOf("-1"),  Float.valueOf("-1"),  Severity.MINOR},
                {Float.valueOf("7.0"),  Float.valueOf("-1"),  Float.valueOf("-1"),  Severity.MINOR},
                {Float.valueOf("6.9"),  Float.valueOf("-1"),  Float.valueOf("-1"),  Severity.MINOR},
                {Float.valueOf("4.0"),  Float.valueOf("-1"),  Float.valueOf("-1"),  Severity.MINOR},
                {Float.valueOf("3.9"),  Float.valueOf("-1"),  Float.valueOf("-1"),  Severity.MINOR},
                {Float.valueOf("1.9"),  Float.valueOf("-1"),  Float.valueOf("-1"),  Severity.MINOR},
                {Float.valueOf("0.0"),  Float.valueOf("-1"),  Float.valueOf("-1"),  Severity.MINOR}
        });
    }

    @Test
    public void testCvssToSonarQubeSeverity() {
        assertThat(DependencyCheckUtils.cvssToSonarQubeSeverity(this.cvssSeverity, this.critical, this.major)).isEqualTo(this.expectedSeverity);
    }

}
