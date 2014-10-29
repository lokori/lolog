# lolog

Logger middleware for Ring applications. This is mainly useful with embedded HTTP containers like http-kit.

## Usage

Simply put it in the middleware chain. Keyword-params middleware is required.
```
  wrap-log-request
  wrap-keyword-params
```

Controlling logging to write a http_access.log file or something else is left to tools.logging which usually delegates this to log4j. 

## Latest from Clojars

[![Clojars Project](http://clojars.org/org.clojars.lokori/lolog/latest-version.svg)](http://clojars.org/org.clojars.lokori/lolog)


## Why?

There are others, yes. This is simple.

The unique id + duration + URI can be used to make simple performance statistics from the log. This is something I find valuable but haven't had
time to write the analyzer yet.

## License

Copyright © 2013 Antti Virtanen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
