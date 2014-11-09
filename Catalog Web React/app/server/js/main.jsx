goog.provide('server.main');

goog.require('server.DiContainer');

/**
 @param {Object} config
 */
server.main = function (config) {
  var container = new server.DiContainer;

  container.configure(
    {
      resolve: server.App,
      "with": {
        config: config,
        express: require('express')
      }
    },
    {
      resolve: server.Middleware,
      "with": {
        bodyParser: require('body-parser'),
        compression: require('compression'),
        methodOverride: require('method-override')
      }
    },
    {
      resolve: server.FrontPage,
      "with": {
        isDev: config['env']['development'],
        clientData: {}
      }
    }
  );

  return container.resolveServerApp();
};

goog.exportSymbol('server.main', server.main);
