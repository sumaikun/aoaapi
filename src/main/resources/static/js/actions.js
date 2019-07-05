angular.module('aoacustomers').service('actions', function (flux) {
  return flux.createActions([
    'getUserWarrantyStatus',
    'signOut',
    'checkCustomerStatus',
    'cancelFetching',
    'loadUserForm',
    'findCities',
    'persistCustomer',
    'getFranchises',
    'generateCreditCardWarranty',
    'generateConsignmentCardWarranty'
  ]);
});
