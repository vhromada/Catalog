goog.provide('server.FrontPage');

/**
 @param {server.react.App} serverApp
 @param {app.react.App} app
 @param {Object} clientData
 @param {boolean} isDev
 @constructor
 */
server.FrontPage = function (serverApp, app, clientData, isDev) {
  this.serverApp = serverApp;
  this.app = app;
  this.clientData = clientData;
  this.isDev = isDev;
};

/**
 @return {string} HTML send to client
 */
server.FrontPage.prototype.render = function () {
  var appHtml = React.renderComponentToString(<this.app.component/>);
  var scriptsHtml = this.getScriptsHtml();
  var bodyHtml = appHtml + scriptsHtml;

  var html = React.renderToStaticMarkup(<this.serverApp.component bodyHtml={bodyHtml}/>);

  return '<!DOCTYPE html>' + html;
};

/**
 @return {string}
 */
server.FrontPage.prototype.getScriptsHtml = function () {
  var scripts = ['/app/client/build/app.js'];
  if (this.isDev) {
    scripts.push.apply(scripts,
      ['/bower_components/closure-library/closure/goog/base.js',
        '/tmp/deps.js',
        '/app/client/js/main.js',
        'http://localhost:35729/livereload.js']
    );
  }

  var html = scripts.map(function (script) {
    return "<script src=\"" + script + "\"></script>";
  }).join('');

  return html + ("<script>app.main(" + (JSON.stringify(this.clientData)) + ");</script>");
};
