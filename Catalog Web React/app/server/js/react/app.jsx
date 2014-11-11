goog.provide('server.react.App');

/**
 @constructor
 */
server.react.App = function () {
  var dom = React.DOM;
  var html = dom.html;
  var head = dom.head;
  var meta = dom.meta;
  var title = dom.title;
  var link = dom.link;
  var body = dom.body;

  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <html lang='en'>
          <head>
            <meta charSet='utf-8'/>
            <meta name='viewport' content='width=device-width, initial-scale=1.0, minimal-ui, maximum-scale=1.0, user-scalable=no'/>
            <title>Catalog</title>
            <link href='/app/client/build/app.css' rel='stylesheet'/>
          </head>
          <body dangerouslySetInnerHTML={{__html: this.props.bodyHtml}}/>
        </html>
      );
    }
  }));
};
