/*
 * Copyright 2016 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.text.write.tables;

import java.util.ArrayList;
import java.util.List;

import org.rf.ide.core.testdata.model.table.SettingTable;
import org.rf.ide.core.testdata.text.write.DumperHelper;
import org.rf.ide.core.testdata.text.write.SectionBuilder.SectionType;
import org.rf.ide.core.testdata.text.write.tables.settings.DefaultTagsDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.ForceTagsDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.LibraryImportDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.MetadataDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.ResourceImportDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.SuiteDocumentationDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.SuiteSetupDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.SuiteTeardownDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.SuiteTestTemplateDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.SuiteTestTimeoutDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.TestSetupDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.TestTeardownDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.UnknownSettingDumper;
import org.rf.ide.core.testdata.text.write.tables.settings.VariablesImportDumper;

public class SettingsSectionTableDumper extends ANotExecutableTableDumper<SettingTable> {

    public SettingsSectionTableDumper(final DumperHelper aDumpHelper) {
        super(aDumpHelper, getDumpers(aDumpHelper), true);
    }

    private static List<ISectionElementDumper<SettingTable>> getDumpers(final DumperHelper aDumpHelper) {
        final List<ISectionElementDumper<SettingTable>> dumpers = new ArrayList<>();
        dumpers.add(new SuiteDocumentationDumper(aDumpHelper));
        dumpers.add(new SuiteSetupDumper(aDumpHelper));
        dumpers.add(new SuiteTeardownDumper(aDumpHelper));
        dumpers.add(new TestSetupDumper(aDumpHelper));
        dumpers.add(new TestTeardownDumper(aDumpHelper));
        dumpers.add(new ForceTagsDumper(aDumpHelper));
        dumpers.add(new DefaultTagsDumper(aDumpHelper));
        dumpers.add(new SuiteTestTemplateDumper(aDumpHelper));
        dumpers.add(new SuiteTestTimeoutDumper(aDumpHelper));
        dumpers.add(new MetadataDumper(aDumpHelper));
        dumpers.add(new LibraryImportDumper(aDumpHelper));
        dumpers.add(new ResourceImportDumper(aDumpHelper));
        dumpers.add(new VariablesImportDumper(aDumpHelper));
        dumpers.add(new UnknownSettingDumper(aDumpHelper));

        return dumpers;
    }

    @Override
    public SectionType getSectionType() {
        return SectionType.SETTINGS;
    }
}
