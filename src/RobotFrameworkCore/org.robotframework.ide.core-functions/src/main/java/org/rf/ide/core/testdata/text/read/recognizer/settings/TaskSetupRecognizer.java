/*
 * Copyright 2018 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.text.read.recognizer.settings;

import java.util.regex.Pattern;

import org.rf.ide.core.testdata.model.RobotVersion;
import org.rf.ide.core.testdata.text.read.recognizer.ATokenRecognizer;
import org.rf.ide.core.testdata.text.read.recognizer.RobotTokenType;

public class TaskSetupRecognizer extends ATokenRecognizer {

    public static final Pattern EXPECTED = Pattern.compile(
            "[ ]?" + createUpperLowerCaseWord("Task") + "[\\s]+" + createUpperLowerCaseWord("Setup") + "([\\s]*:)?");

    public TaskSetupRecognizer() {
        super(EXPECTED, RobotTokenType.SETTING_TASK_SETUP_DECLARATION);
    }

    @Override
    public boolean isApplicableFor(final RobotVersion robotVersion) {
        return robotVersion.isNewerOrEqualTo(new RobotVersion(3, 1));
    }

    @Override
    public ATokenRecognizer newInstance() {
        return new TaskSetupRecognizer();
    }
}
