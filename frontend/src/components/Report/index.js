import React from 'react'

class Report extends React.Component {
    constructor(props) {
      super(props);
      this.jobId = props.jobId
      this.report = props.report
    }

    render() {
      return (
        <div>
           {this.report >= 0 && (
             <div>{this.report} for {this.jobId}</div>
           )}
           {this.report < 0 && (
             <div>Limit free request</div>
           )}
        </div>
      );
    }
  }

  export default Report