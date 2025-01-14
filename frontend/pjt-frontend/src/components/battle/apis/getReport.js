import axios from "axios";

function getReport(userId) {
  axios({
    method: "GET",
    url: `/profile/report/${userId}`,
    data: {
      userId,
    },
  })
    .then((response) => {
      console.log(response);
    })
    .catch((error) => {
      console.log(error);
    });
}

export default getReport;
