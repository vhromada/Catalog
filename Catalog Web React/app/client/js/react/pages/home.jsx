goog.provide('app.react.pages.Home');

/**
 @constructor
 */
app.react.pages.Home = function () {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <header>Catalog</header>
      );
    }
  }));
};
