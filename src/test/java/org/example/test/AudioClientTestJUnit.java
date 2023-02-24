package org.example.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AudioClientTestJUnit {
  @Test
  public void testMainMethod() throws Exception {
    AudioClientTest.main(null);
    LineChartPlotter.main(null);
  }
}
