package org.whispersystems.contactdiscovery.redis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.util.Pool;

public class LuaScript {

  private final Pool<Jedis> jedisPool;
  private final String      script;
  private final byte[]      sha;

  public static LuaScript fromResource(Pool<Jedis> jedisPool, String resource) throws IOException {
    InputStream           inputStream = LuaScript.class.getClassLoader().getResourceAsStream(resource);
    ByteArrayOutputStream baos        = new ByteArrayOutputStream();

    byte[] buffer = new byte[4096];
    int    read;

    while ((read = inputStream.read(buffer)) != -1) {
      baos.write(buffer, 0, read);
    }

    inputStream.close();
    baos.close();

    return new LuaScript(jedisPool, new String(baos.toByteArray()));
  }

  private LuaScript(Pool<Jedis> jedisPool, String script) {
    this.jedisPool = jedisPool;
    this.script = script;
    this.sha = storeScript(jedisPool, script).getBytes();
  }

  public Object execute(List<byte[]> keys, List<byte[]> args) {
    try (Jedis jedis = jedisPool.getResource()) {
      try {
        return jedis.evalsha(sha, keys, args);
      } catch (JedisDataException e) {
        storeScript(jedisPool, script);
        return jedis.evalsha(sha, keys, args);
      }
    }
  }

  private String storeScript(Pool<Jedis> jedisPool, String script) {
    try (Jedis jedis = jedisPool.getResource()) {
      return jedis.scriptLoad(script);
    }
  }

}
