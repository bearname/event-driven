import React from 'react'
import Report from "../Report"

class JobForm extends React.Component {
    constructor(props) {
      super(props);
      this.state = { jobId: false, showForm: true, showRedirect: false,  showJobResult: false, jobResult: 0.0 };
      this.regionRef = React.createRef();
      this.backendHost = process.env.API_URL; 
    }

    handleChange = (event) => {
      this.setState({[event.target.name]: event.target.value});
    }
  
    handleSubmit = async (event) => {
      event.preventDefault()
      const json = {
        "description": this.state.description,
        "region":  event.target.regionCode.value
      }
      const options = {
        method: 'POST',
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(json)
      }
      const api = `${process.env.REACT_APP_API_URL}api/job/create`;
      console.log("backend: " + api)
      const response = await fetch(api, options)
      const data = await response.json()

      console.log(data)
      if (data.hasOwnProperty("jobId")) {
        this.setState({ jobId: data.jobId, showForm: false, showRedirect: true })
      }
    }
    
    onTextDetails = async(event) => {
      event.preventDefault()
      const options = {
        method: 'GET',
        headers: {
          "Content-Type": "application/json",
        },
      }
      const api = `${process.env.REACT_APP_API_URL}/api/job/text-details?jobId=`  + this.state.jobId;
      console.log("backend: " + api)
      const response = await fetch(api, options) 
      const data = await response.json()
      console.log(data)

      if (data.hasOwnProperty("status")) {
        console.log(data.status)
        if (data.hasOwnProperty("result")) {
          console.log(data.result)
          this.setState( {jobResult: data.result, showJobResult:true, showRedirect: false})
        } 
      }
    }

    onReset = async(event) => {
      event.preventDefault()
      this.setState({jobId: false, showForm: true, showRedirect: false,  showJobResult: false, jobResult: 0.0})
    }

    render() {
      return (
        <div>
           <button type="button" onClick={this.onReset}>reset</button>
           {this.state.showJobResult && (
              <Report report={this.state.jobResult}  jobId = {this.state.jobId}/>
           )}
           {this.state.showRedirect && (
             <button type="button" onClick={this.onTextDetails}>{this.state.jobId}</button>
           )}
            
          {this.state.showForm && (<form onSubmit={this.handleSubmit}>
            <label for="regionCode">Region :</label>
            <select id="regionCode" name="regionCode">
              <option value="RUS">Russia</option>
              <option value="EU">European Union</option>
              <option value="USA">USA</option>
            </select>
            <label>
              Job description:
              <input type="text" value={this.state.value} name="description" onChange={this.handleChange} />
            </label>
            <input type="submit" value="Submit" />
          </form>)
          }
        </div>
      );
    }
  }

  export default JobForm