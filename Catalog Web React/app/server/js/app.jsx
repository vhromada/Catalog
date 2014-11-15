goog.provide('server.App');

goog.require('goog.labs.userAgent.util');

/**
 * @param {Function} express
 * @param {Object} config
 * @param {app.Routes} routes
 * @param {server.FrontPage} frontPage
 * @param {server.Storage} storage
 * @param {Function} compression
 * @param {Function} bodyParser
 * @param {Function} methodOverride
 * @constructor
 */
server.App = function (express, config, routes, frontPage, storage, compression, bodyParser, methodOverride) {
  var app = express();

  app.use(compression());
  app.use(bodyParser.json());
  app.use(methodOverride());

  if (config['env']['development']) {
    app.use('/bower_components', express['static']('bower_components'));
    app.use('/app', express['static']('app'));
    app.use('/tmp', express['static']('tmp'));
  } else {
    app.use('/app', express["static"]('app'));
  }

  var onError = function (route, reason) {
    console.log('Error: ' + '500');
    console.log('Route path: ' + route.path);
    console.log('Reason:');
    if (reason.stack) {
      return console.log(reason.stack);
    } else {
      return console.log(reason);
    }
  };

  routes.addToExpress(app, function (route, req, res) {
    var params = req.params;
    return storage.load(route, params).then(function () {
      return routes.setActive(route, params);
    }).thenCatch(function (reason) {
      return routes.trySetErrorRoute(reason);
    }).then(function () {
      goog.labs.userAgent.util.setUserAgent(req.headers['user-agent']);
      return frontPage.render();
    }).then(function (html) {
      var status = routes.active === routes.notFound ? 404 : 200;
      return res.status(status).send(html);
    }).thenCatch(function (reason) {
      onError(route, reason);
      return res.status(500).send('Server error.');
    });
  });

  var port = config['server']['port'];
  app.listen(port);
  console.log('Express server listening on port ' + port);
};
