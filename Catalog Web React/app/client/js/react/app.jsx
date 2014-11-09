goog.provide('app.react.App');

/**
 @param {app.Routes} routes
 @param {app.react.Header} header
 @constructor
 */
app.react.App = function (routes, header) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <div className="app">
          <header.component/>
        </div>
      );
    }
  }));
};
