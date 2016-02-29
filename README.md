[![Build Status](https://travis-ci.org/daedafusion/cache.svg?branch=master)](https://travis-ci.org/daedafusion/cache)

[![Coverage Status](https://coveralls.io/repos/github/daedafusion/cache/badge.svg?branch=master)](https://coveralls.io/github/daedafusion/cache?branch=master)

# cache

Pluggable cache framework using [Service Framework](https://github.com/daedafusion/service-framework)

## Maven

```xml
<dependency>
    <groupId>com.daedafusion</groupId>
    <artifactId>cache-core</artifactId>
    <version>1.0</version>
</dependency>
```

# Providers

`cache-ehcache` is a simple memory only cache backed by ehcache.

## Framework Configuration

```xml
<dependency>
    <groupId>com.daedafusion</groupId>
    <artifactId>cache-ehcache</artifactId>
    <version>1.0</version>
    <classifier>plugin</classifier>
    <type>zip</type>
</dependency>
```

    managedObjectDescriptions:
    - infClass: com.df.argos.commons.cache.CacheManager
      implClass: com.df.argos.commons.cache.impl.CacheManagerImpl
    
    - infClass: com.df.argos.commons.cache.providers.CacheManagerProvider
      implClass: com.df.argos.commons.cache.providers.MemoryOnlyEhcacheManager
      loaderUri: framework://loader/cache-ehcache/
    
    loaderDescriptions:
    - uri: framework://loader/cache-ehcache/
      loaderClass: com.df.argos.commons.sf.loader.impl.ZipLoader
      resource: file:///opt/argos/plugins/cache-ehcache-plugin.zip
      properties: {pluginName: cache-ehcache}