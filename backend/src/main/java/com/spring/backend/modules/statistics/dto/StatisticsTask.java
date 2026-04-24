package com.spring.backend.modules.statistics.dto;

public interface StatisticsTask {
  Long getTotalTasks();

  Long getJoinedTasks();

  Long getNotJoinedTasks();
}
