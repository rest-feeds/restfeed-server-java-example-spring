package org.restfeeds.server.database;

import java.util.List;
import org.restfeeds.server.FeedItem;
import org.restfeeds.server.FeedItemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcFeedItemRepository implements FeedItemRepository {

  private final JdbcTemplate jdbcTemplate;
  private final FeedItemMapper feedItemMapper;

  public JdbcFeedItemRepository(JdbcTemplate jdbcTemplate, FeedItemMapper feedItemMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.feedItemMapper = feedItemMapper;
  }

  @Override
  public void append(
      String feed,
      String id,
      String type,
      String uri,
      String method,
      String timestamp,
      Object data) {

    String dataAsString = DataSerializer.toString(data);

    String sql =
        "insert into feed (feed, id, type, uri, method, timestamp, data) values (?, ?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, feed, id, type, uri, method, timestamp, dataAsString);
  }

  @Override
  public List<FeedItem> findByFeedPositionGreaterThanEqual(String feed, long position, int limit) {
    String sql = "select * from feed where feed = ? and position >= ? limit ?";
    return jdbcTemplate.query(sql, new Object[] {feed, position, limit}, feedItemMapper);
  }
}
