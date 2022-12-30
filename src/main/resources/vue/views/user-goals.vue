<template id="user-goals">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            User Goals
          </div>

          <div class="col" align="right" v-if="goals.length === 0">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
          <div class="col" align="right" v-else>
            <div class="p2">
            <button rel="tooltip" title="Update" @click=onClick() class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteGoal()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
            </div>

          </div>
        </div>
      </div>

      <div v-if="goals.length === 0">
        <br>
        <p style="margin-left: 15px"> No Goals added yet! Create goals by clicking the add button.</p>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addUserGoals">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-weight">Target Weight</span>
            </div>
            <input type="text" class="form-control" v-model="formData.targetWeight" name="targetWeight" placeholder="Target Weight"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-calories">Target Calories</span>
            </div>
            <input type="text" class="form-control" v-model="formData.targetCalories" name="targetCalories" placeholder="Target Calories"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-level">Target Level</span>
            </div>
            <input type="text" class="form-control" v-model="formData.targetLevel" name="targetLevel" placeholder="Target Level"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-primary float-right" @click="addUserGoals()">Add Goals</button>
      </div>
    </div>



    <div class="card bg-light mb-3" v-if="goals.length !== 0">

      <div class="card-body" v-for="(goal,index) in goals" v-bind:key="index">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-weight">User ID</span>
            </div>
            <input type="text" class="form-control" v-model="goal.userId" name="targetWeight" readonly/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-weight">Target Weight</span>
            </div>
            <input type="text" class="form-control" id="targetWeight" v-model="goal.targetWeight" :disabled="disabled" name="targetWeight" placeholder="Target Weight"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-calories">Target Calories</span>
            </div>
            <input type="text" class="form-control" id="targetCalories" v-model="goal.targetCalories" :disabled="disabled" name="targetCalories" placeholder="Target Calories"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-level">Target Level</span>
            </div>
            <input type="text" class="form-control" id="targetLevel" v-model="goal.targetLevel" :disabled="disabled" name="targetLevel" placeholder="Target Level"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-target-level">Date</span>
            </div>
            <input type="text" class="form-control" v-model="goal.date.toString().slice(0,10)" name="targetLevel" readonly/>
          </div>
        </form>
      </div>
      <div class="card-footer text-center" v-if="!disabled">
        <button rel="tooltip" title="Update" class="btn btn-primary center" @click="updateUserGoals()">Update Goals</button>
      </div>

      </div>





  </app-layout>
</template>
<script>
Vue.component("user-goals", {
  template: "#user-goals",
  data: () => ({
    goals: [],
    formData: [],
    hideForm: true,
    disabled: true
  }),
  created() {
    this.getUserGoals();
  },
  methods: {
    onClick: function () {
      this.disabled=false
    },
    getUserGoals: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}/goals`;
      axios.get(url)
          .then(res => this.goals = res.data)
          .catch(() => console.log("Error while fetching goals"));
    },
    deleteGoal: function () {
      if (confirm('Are you sure you want to delete?')) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}/goals`;
        axios.delete(url)
            .then(response =>
                this.goals = [])
            .catch(function (error) {
              console.log(error)
            })
      }
    },
    addUserGoals: function (){
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}/goals`;
      axios.post(url,
          {
            targetWeight: this.formData.targetWeight,
            targetCalories: this.formData.targetCalories,
            targetLevel: this.formData.targetLevel,
            date: new Date().toISOString(),
            userId: userId
          })
          .then(response => {
            this.goals.push(response.data)
            this.hideForm= true;
            this.disabled=true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    updateUserGoals: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}/goals`
      const data={
        targetWeight: parseInt(document.getElementById("targetWeight").value)
          ,
          targetCalories: parseInt(document.getElementById("targetCalories").value),
          targetLevel: parseInt(document.getElementById("targetLevel").value),
          date: new Date().toISOString(),
          userId: userId}
      axios.patch(url,
          {
            data
          })
          .then(response =>
          {this.goals.pop()
            this.goals.push(data)
            this.disabled=true})
          .catch(error => {
            console.log(error)
          })
    },
  }
});
</script>