package com.github.hekonsek.rxjava.connector.slack;

import lombok.Data;

import java.util.List;

@Data
public class SlackTable {

    private final String title;

    private final List<String> columns;

    private final List<List<Object>> rows;

}