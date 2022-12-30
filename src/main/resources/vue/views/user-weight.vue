<template id="user-weight">
  <app-layout>
      <h4>Weight Tracker</h4>
    <br>
    <div class="chart-container"
         style="position: relative; height:40vh; width:85vw; border: 1px solid lightgrey; padding: 25px">
      <canvas id="myChart"></canvas>
    </div>
<br>
    <p v-if="weight.length === 0">No Data Found! Add your weight now to track health status</p>
<br>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Timeline </div>


        <div class="col" align="right">
          <button rel="tooltip" title="Add"
                  class="btn shadow-none border-0" data-toggle="modal" data-target="#myModal"
                  >
            <b-icon-plus-circle variant="secondary" class="mr-2"></b-icon-plus-circle>
          </button>
        </div>
        </div>
      </div>


      <!-- Modal -->
      <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="modalTitle">Today</h5>
              <button type="button" class="close" data-dismiss="modal">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <form ref="myForm">
                <div class="form-group">
                  <label for="inpWeight">Current weight(kgs)</label>
                  <input type="text" class="form-control" id="inpWeight" v-model="formData.value" placeholder="Enter your weight...">
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
              <button type="button" data-dismiss="modal" class="btn btn-primary" @click="addWeight();">Save changes</button>
            </div>
          </div>
        </div>
      </div>


      <!-- Modal -->
      <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="modalTitle">{{new Date(currentDate).toLocaleDateString('en-us', { weekday: "long", year:"numeric", month:"short", day:"numeric"})}}</h5>
              <button type="button" class="close" data-dismiss="modal">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <form>
                <div class="form-group">
                  <label for="inpWeight">Enter new weight(kgs)</label>
                  <input type="text" class="form-control" id="inpWeight" v-model="formData.value">
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
              <button type="button" data-dismiss="modal" class="btn btn-primary" @click="updateWeight(currentId, currentDate);">Save changes</button>
            </div>
          </div>
        </div>
      </div>


      <div class="card-body" v-for="(w, index) in weight" v-bind:key="index">
        <table class="table">
          <tbody>
          <tr>
            <th scope="row">{{ w.value }} kgs</th>
            <td>{{ new Date(w.date).toLocaleDateString('en-us', { weekday: "long", year:"numeric", month:"short", day:"numeric"}) }}</td>
            <td class="float-right">
              <button rel="tooltip" title="Update" @click=getData(w) data-toggle="modal" data-target="#updateModal"
                        class="btn btn-info btn-simple btn-link"
                        >
                <b-icon-pencil-square variant="secondary" class="mr-2"></b-icon-pencil-square>
            </button>
              <button rel="tooltip" title="Delete"
                      class="btn btn-info btn-simple btn-link"
                      @click="deleteWeight(w, index)">
                <b-icon-trash-fill variant="secondary" class="mr-2"></b-icon-trash-fill>
              </button>
            </td>
          </tr>
          </tbody>
        </table>
    </div>
    </div>


  </app-layout>
</template>

<script>
Vue.component("user-weight",{
  template: "#user-weight",
  data: () => ({
    weight: [],
    dates: [],
    values: [],
    formData: [],
    currentId: null,
    currentDate: null
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/weight`)
        .then(res => {
          this.weight = res.data
          if(this.weight.length) {
            const ctx = document.getElementById('myChart');
            for (item of this.weight) {
              this.dates.push(new Date(item.date).toLocaleDateString('en-us', { year:"numeric", month:"short", day:"numeric"}) )
              this.values.push(item.value)
            }
            this.dates.sort(function(a, b) {
              return new Date(a) - new Date(b);
            });
            new Chart(ctx, {
              type: 'line',
              data: {
                labels: this.dates,
                datasets: [{
                  label: '# of Votes',
                  data: this.values,
                  borderWidth: 1
                }]
              },
              options: {
                scales: {
                  y: {
                    beginAtZero: false
                  },
                  x: {
                    grid: {
                      display: false,
                    }
                  },
                  y: {
                    grid: {
                      display: false
                    }
                  },
                },
                plugins: {
                  legend: {
                    display: false
                  }
                }
              }
            })
          }

        })
        .catch(() => console.log("Error while fetching weight"));

  },
  methods: {
    getData: function (w) {

        this.currentId=w.id;
        this.currentDate=w.date;

    },
    addWeight: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}/weight`
      axios.post(url,
          {
            value: parseFloat(this.formData.value),
            date: new Date().toISOString(),
            userId: userId
          })
          .then(response =>
              this.weight.push(response.data))
          .catch(error => {
            console.log(error)
          })

    },
    updateWeight: function (id, date) {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/weight/${id}`
      axios.patch(url,
          {
            value: parseFloat(this.formData.value),
            date: date,
            userId: userId
          })
          .then(response =>
              this.weight.push(response.data))
          .catch(error => {
            console.log(error)
          })
    },
    deleteWeight: function (weight, index) {
      if (confirm('Are you sure you want to delete?')) {
        const url = `/api/weight/${weight.id}`;
        axios.delete(url)
            .then(response =>
                this.weight.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
  },



});



</script>