/*
 * Copyright 2018 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.mapping.setting.task;

import java.util.List;
import java.util.Stack;

import org.rf.ide.core.testdata.mapping.table.IParsingMapper;
import org.rf.ide.core.testdata.mapping.table.ParsingStateHelper;
import org.rf.ide.core.testdata.model.FilePosition;
import org.rf.ide.core.testdata.model.RobotFileOutput;
import org.rf.ide.core.testdata.model.RobotVersion;
import org.rf.ide.core.testdata.model.table.SettingTable;
import org.rf.ide.core.testdata.model.table.setting.TaskTimeout;
import org.rf.ide.core.testdata.text.read.ParsingState;
import org.rf.ide.core.testdata.text.read.RobotLine;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;
import org.rf.ide.core.testdata.text.read.recognizer.RobotTokenType;

public class TaskTimeoutMessageMapper implements IParsingMapper {

    private final ParsingStateHelper utility = new ParsingStateHelper();

    @Override
    public boolean isApplicableFor(final RobotVersion robotVersion) {
        return robotVersion.isNewerOrEqualTo(new RobotVersion(3, 1));
    }

    @Override
    public boolean checkIfCanBeMapped(final RobotFileOutput robotFileOutput, final RobotLine currentLine,
            final RobotToken rt, final String text, final Stack<ParsingState> processingState) {

        final ParsingState currentState = utility.getCurrentState(processingState);
        if (currentState == ParsingState.SETTING_TASK_TIMEOUT) {
            final List<TaskTimeout> taskTimeouts = robotFileOutput.getFileModel().getSettingTable().getTaskTimeouts();
            return checkIfHasAlreadyValue(taskTimeouts);
        }
        return currentState == ParsingState.SETTING_TASK_TIMEOUT_VALUE
                || currentState == ParsingState.SETTING_TASK_TIMEOUT_MESSAGE_ARGUMENTS;
    }

    private boolean checkIfHasAlreadyValue(final List<TaskTimeout> taskTimeouts) {
        return !taskTimeouts.isEmpty() && taskTimeouts.get(taskTimeouts.size() - 1).getTimeout() != null;
    }

    @Override
    public RobotToken map(final RobotLine currentLine, final Stack<ParsingState> processingState,
            final RobotFileOutput robotFileOutput, final RobotToken rt, final FilePosition fp, final String text) {

        rt.getTypes().add(0, RobotTokenType.SETTING_TASK_TIMEOUT_MESSAGE);
        rt.setText(text);

        final SettingTable settings = robotFileOutput.getFileModel().getSettingTable();
        final List<TaskTimeout> timeouts = settings.getTaskTimeouts();
        if (!timeouts.isEmpty()) {
            timeouts.get(timeouts.size() - 1).addMessageArgument(rt);
        }

        processingState.push(ParsingState.SETTING_TASK_TIMEOUT_MESSAGE_ARGUMENTS);
        return rt;
    }
}
