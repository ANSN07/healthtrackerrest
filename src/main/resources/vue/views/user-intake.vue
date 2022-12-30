<template id="user-intake">
  <app-layout>

    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <h3 class="col-6">
            Food Intakes
          </h3>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
    </div>


    <div class="card bg-light mb-3" :class="{ 'd-none': hideForm}">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Add Food Intake </div>
        </div>
      </div>

      <div class="card-body">

        <form>
          <div class="form-group">
            <label for="inputAddress">User ID</label>
            <input type="text" class="form-control" id="inputAddress" readonly v-model="userId">
          </div>
          <div class="form-group">
            <label for="activity">Meal</label>
            <select class="form-select" id="activity" v-model="formData.mealType">
              <option selected value="Breakfast">Breakfast</option>
              <option value="Lunch">Lunch</option>
              <option value="Dinner">Dinner</option>
            </select>
          </div>


          <div class="form-row">
            <div class="form-group col-md-6">
              <label for="inputTime">Food Item</label>
              <input type="text" class="form-control" v-model="formData.foodName" id="inputFood">
            </div>
            <div class="form-group col-md-6">
              <label for="inputDistance">Calorie</label>
              <input type="text" class="form-control" id="inputcalorie" v-model="formData.calorie" placeholder="Estimated calorie intake...">
            </div>
          </div>
          <div class="form-row">
            <div class="form-group col-md-6">
              <label for="inputCal">Unit Measure</label>
              <input type="text" class="form-control" id="unit" v-model="formData.unitMeasure">
            </div>
            <div class="form-group col-md-6">
              <label for="inputCal">Number of units</label>
              <input type="text" class="form-control" id="unit" v-model="formData.numberOfItems">
            </div>
          </div>

          <button @click="addData()" class="btn btn-primary">Add Intake</button>

        </form>
      </div>
    </div>

    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Intakes List </div>
        </div>
      </div>

      <div class="card-body" v-if="intakes.length!==0">
        <div v-for="(intake, index) in intakes">
          <div class="card card-body bg-light-gray" style="margin: 15px">
            <h5 class="float-left"><b-icon-check-all class="mr-2"></b-icon-check-all>
              {{intake.mealType.toUpperCase()}} : {{intake.date}}
            </h5>

            <div class="col" align="right" >
              <button rel="tooltip" title="Delete"
                      class="btn btn-primary"
                      @click="deleteFoodIntake(intake, index, foodItems[index])">
                <b-icon-trash-fill variant="danger" class="mr-2"></b-icon-trash-fill> DELETE INTAKE
              </button>
            </div>
            <div style="margin-left: 35px">
              <span>{{foodItems[index].foodName.toUpperCase()}} ({{ foodItems[index].calorie }} calories)</span> <br>
              <span>Consumed {{foodItems[index].numberOfItems}} {{foodItems[index].unitMeasure}}</span>
            </div>
          </div>
        </div>

      </div>


      <p style="margin-left: 20px; margin-top: 20px" v-else>No data found! Please add by clicking the create button.</p>
    </div>





  </app-layout>
</template>

<script>
Vue.component("user-intake",{
  template: "#user-intake",
  data: () => ({
    intakes: [],
    userId: null,
    hideForm :true,
    formData: [],
    foodItems: [],
    foodIntakes: []
  }),
  created() {
    this.userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${this.userId}/intakes`)
        .then(res => this.intakes = res.data)
        .catch(() => console.log("Error while fetching intakes"));
    axios.get(`/api/users/${this.userId}/food-items`)
        .then(res => {
              this.foodItems = res.data
            }
        )
        .catch(() => console.log("Error while fetching food items"));
  },
  methods: {
    addData: function () {
      this.addIntake()
      this.addFoodItem()
    },
    addIntake: function () {
      const url = `/api/intakes`;
      const userId = this.$javalin.pathParams["user-id"];
      axios.post(url,
          {
            mealType : this.formData.mealType,
            date : new Date().toISOString(),
            userId: userId
          })
          .then(response => {
            this.intakes.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    addFoodItem: function (){
      const url = `/api/food`;
      const userId = this.$javalin.pathParams["user-id"];
      axios.post(url,
          {
            foodName : this.formData.foodName,
            calorie : this.formData.calorie,
            unitMeasure : this.formData.unitMeasure,
            numberOfItems : this.formData.numberOfItems,
            userId: userId
          })
          .then(response => {
            this.foodItems.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteFoodIntake: function (intake, index, foodItem ) {
      if (confirm('Are you sure you want to delete?')) {
        const urlForIntake = `/api/intakes/${intake.intakeId}`;
        axios.delete(urlForIntake)
            .then(response =>
                this.intakes.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
        const url = `/api/food/${foodItem.foodId}`;
        axios.delete(url)
            .then(response =>
                this.foodItems.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
  }
});
</script>